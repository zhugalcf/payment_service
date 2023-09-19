package faang.school.paymentservice.service;

import faang.school.paymentservice.exception.NoFreeAccountNumber;
import faang.school.paymentservice.exception.TypeNotFoundException;
import faang.school.paymentservice.model.AccountNumbersSequence;
import faang.school.paymentservice.model.AccountType;
import faang.school.paymentservice.model.FreeAccountNumber;
import faang.school.paymentservice.repository.AccountNumbersSequenceRepository;
import faang.school.paymentservice.repository.FreeAccountNumberRepository;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


@Service
@RequiredArgsConstructor
@Slf4j
public class FreeAccountNumberService {
    private final FreeAccountNumberRepository freeAccountNumberRepository;
    private final AccountNumbersSequenceRepository accountNumberSequenceRepository;
    private final static int coreCount = Runtime.getRuntime().availableProcessors();

    public void getFreeNumber(AccountType accountType, Consumer<String> action) {
        FreeAccountNumber freeNumber = freeAccountNumberRepository.getFreeAccountNumber(accountType.getValue())
                .orElseThrow(() -> {
                    throw new NoFreeAccountNumber(accountType);
                });
        action.accept(MessageFormat.format("Generated number {}", freeNumber.getAccountNumber()));
    }

    @Transactional
    @Retryable(
            retryFor = PersistenceException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 3000))
    public void generateFreeAccNumber(AccountType accountType){
        AccountNumbersSequence accountNumbersSequence =
                accountNumberSequenceRepository.findByAccountType(accountType.getValue())
                        .orElseThrow(() -> new TypeNotFoundException(accountType));

        BigInteger currentCount = accountNumberSequenceRepository.incrementCounter(accountType.getValue()).orElseThrow(
                () -> {
                    String errorMessage = "Failed to increment count for: " + accountType;
                    log.info(errorMessage);
                    return new RuntimeException(errorMessage);
                }
        );

        accountNumberSequenceRepository.save(accountNumbersSequence);

        String accountNumber = String.format("%s%020d", accountType.getValue(), currentCount);

        FreeAccountNumber freeAccountNumber = FreeAccountNumber.builder()
                .id(new FreeAccountNumber.FreeAccountNumberKey(accountType.getValue(),new BigInteger(accountNumber)))
                .accountType(accountType.getValue())
                .accountNumber(new BigInteger(accountNumber))
                .build();

        freeAccountNumberRepository.save(freeAccountNumber);

        log.info("{} is saved to db", freeAccountNumber.getAccountNumber());
    }

    @Recover
    public void handleMaxRetriesReached(Exception e, AccountType accountType) {
        log.info("Maximum retry attempts reached for generateFreeAccNumber with accountType: " + accountType, e);
    }

    //@Scheduled(cron = "0 0 9,13,18 * * ?")
    @Scheduled(fixedRate = 5000)
    void scheduleAccNumberGeneration() {
        log.info("Scheduled task is started");

        ExecutorService executorService = Executors.newFixedThreadPool(coreCount);

        for (int i = 0; i < 25; i++) {
            executorService.submit(() -> generateFreeAccNumber(AccountType.DEBIT));
        }
        for (int i = 0; i < 25; i++) {
            executorService.submit(() -> generateFreeAccNumber(AccountType.SAVINGS));
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}