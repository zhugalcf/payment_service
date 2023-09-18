package faang.school.paymentservice.service;

import faang.school.paymentservice.exception.NoFreeAccountNumber;
import faang.school.paymentservice.exception.TypeNotFoundException;
import faang.school.paymentservice.model.AccountNumbersSequence;
import faang.school.paymentservice.model.AccountType;
import faang.school.paymentservice.model.FreeAccountNumber;
import faang.school.paymentservice.repository.AccountNumbersSequenceRepository;
import faang.school.paymentservice.repository.FreeAccountNumberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.function.Consumer;


@Service
@RequiredArgsConstructor
@Slf4j
public class FreeAccountNumberService {
    private final FreeAccountNumberRepository freeAccountNumberRepository;
    private final AccountNumbersSequenceRepository accountNumberSequenceRepository;

    @Transactional
    public void generateFreeAccNumber(AccountType accountType){
        AccountNumbersSequence accountNumbersSequence =
                accountNumberSequenceRepository.findByAccountType(accountType.getValue())
                .orElseThrow(() -> new TypeNotFoundException(accountType));

        BigInteger currentCount = accountNumberSequenceRepository.incrementCounter(accountType.getValue()).orElseThrow(
                () -> {
                    String errorMessage = "Failed to increment count for: " + accountType;
                    log.error(errorMessage);
                    return new RuntimeException(errorMessage);
                }
        );
        log.info("Current count is {}", currentCount);

        String accountNumber = String.format("%s%020d", accountType.getValue(), currentCount);

        FreeAccountNumber freeAccountNumber = FreeAccountNumber.builder()
                .id(new FreeAccountNumber.FreeAccountNumberKey(accountType.getValue(),new BigInteger(accountNumber)))
                .accountType(accountType.getValue())
                .accountNumber(new BigInteger(accountNumber))
                .build();

        freeAccountNumberRepository.save(freeAccountNumber);

        log.info("{} is saved to db", freeAccountNumber.getAccountNumber());
    }

    public void getFreeNumber(AccountType accountType, Consumer<String> action) {
        FreeAccountNumber freeNumber = freeAccountNumberRepository.getFreeAccountNumber(accountType.getValue())
                .orElseGet(() -> {
                    generateFreeAccNumber(accountType);
                    throw new NoFreeAccountNumber(accountType);
                });
        action.accept(freeNumber.toString());
    }
}