package faang.school.paymentservice.service;

import faang.school.paymentservice.dto.AccountDto;
import faang.school.paymentservice.dto.BalanceDto;
import faang.school.paymentservice.dto.CreatePaymentRequest;
import faang.school.paymentservice.dto.PaymentStatus;
import faang.school.paymentservice.dto.RedisPaymentDto;
import faang.school.paymentservice.dto.UpdateBalanceDto;
import faang.school.paymentservice.exception.BalanceNotFoundException;
import faang.school.paymentservice.mapper.AccountMapper;
import faang.school.paymentservice.mapper.BalanceMapper;
import faang.school.paymentservice.message.publisher.PaymentRequestPublisher;
import faang.school.paymentservice.model.Account;
import faang.school.paymentservice.model.Balance;
import faang.school.paymentservice.model.BalanceAudit;
import faang.school.paymentservice.repository.AccountRepository;
import faang.school.paymentservice.repository.BalanceAuditRepository;
import faang.school.paymentservice.repository.BalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final AccountRepository accountRepository;
    private final BalanceRepository balanceRepository;
    private final BalanceAuditRepository balanceAuditRepository;
    private final AccountMapper accountMapper;
    private final BalanceMapper balanceMapper;
    private final PaymentRequestPublisher paymentRequestPublisher;
    @Value("${scheduler.force-request.limit}")
    private Long limit;

    @Transactional
    public AccountDto createAccount(Long accountNumber){
        Account account = Account.builder().accountNumber(accountNumber).build();
        Account savedAccount = accountRepository.save(account);
        log.info("Account {} is saved to db", account.getId());

        Balance balance = balanceRepository.save(createBalance(savedAccount));
        log.info("Balance {} is saved to db", balance);

        return accountMapper.toDto(savedAccount);
    }

    @Transactional(readOnly = true)
    public BalanceDto getBalance(Long id){
        Balance balance = findBalance(id);
        return balanceMapper.toDto(balance);
    }

    @Transactional
    public BalanceDto updateBalance(UpdateBalanceDto updateBalanceDto){
        Balance balance = findBalance(updateBalanceDto.getBalanceId());

        BigDecimal currentBalance = balance.getCurrentBalance();
        BigDecimal deposit = updateBalanceDto.getDeposit();

        BigDecimal sum = currentBalance.add(deposit);

        balance.setBalanceVersion(balance.getBalanceVersion() + 1);
        balance.setCurrentBalance(sum);
        balance.setUpdated(ZonedDateTime.now());

        log.info("Balance {} is updated", balance);

        return balanceMapper.toDto(balanceRepository.save(balance));
    }

    @Transactional
    @Async(value = "paymentPool")
    public BalanceAudit createRequestForPayment(CreatePaymentRequest createPaymentRequest){
        Balance senderBalance = findBalance(createPaymentRequest.senderBalanceNumber());
        Balance getterBalance = findBalance(createPaymentRequest.getterBalanceNumber());
        BigDecimal deposit = createPaymentRequest.amount();
        BalanceAudit balanceAudit = BalanceAudit.builder()
                .senderBalance(senderBalance)
                .getterBalance(getterBalance)
                .lockValue(String.valueOf(getterBalance.getId() + senderBalance.getId()))
                .amount(deposit)
                .paymentStatus(PaymentStatus.PENDING)
                .created(ZonedDateTime.now())
                .clearScheduledAt(createPaymentRequest.clearScheduledAt())
                .build();
        balanceAudit = balanceAuditRepository.save(balanceAudit);
        RedisPaymentDto redisPaymentDto = new RedisPaymentDto(senderBalance.getId(), getterBalance.getId(),
                deposit, createPaymentRequest.currency(), PaymentStatus.PENDING);
        paymentRequestPublisher.publish(redisPaymentDto);
        return balanceAudit;
    }

    @Transactional
    @Async(value = "paymentPool")
    public BalanceAudit cancelRequestForPayment(Long balanceAuditId){
        BalanceAudit balanceAudit = balanceAuditRepository.findById(balanceAuditId)
                .orElseThrow(() -> new EntityNotFoundException("There is no such request in DB"));
        balanceAudit.setPaymentStatus(PaymentStatus.CANCELED);
        balanceAudit = balanceAuditRepository.save(balanceAudit);
        RedisPaymentDto redisPaymentDto = new RedisPaymentDto(balanceAudit.getSenderBalance().getId(),
                balanceAudit.getGetterBalance().getId(), balanceAudit.getAmount(),
                balanceAudit.getCurrency(), PaymentStatus.CANCELED);
        paymentRequestPublisher.publish(redisPaymentDto);
        return balanceAudit;
    }

    @Transactional
    @Async(value = "paymentPool")
    public BalanceAudit forceRequestForPayment(Long balanceAuditId){
        BalanceAudit balanceAudit = balanceAuditRepository.findById(balanceAuditId)
                .orElseThrow(() -> new EntityNotFoundException("There is no such request in DB"));
        balanceAudit.setPaymentStatus(PaymentStatus.SUCCESS);
        balanceAudit = balanceAuditRepository.save(balanceAudit);
        RedisPaymentDto redisPaymentDto = new RedisPaymentDto(balanceAudit.getSenderBalance().getId(),
                balanceAudit.getGetterBalance().getId(), balanceAudit.getAmount(),
                balanceAudit.getCurrency(), PaymentStatus.SUCCESS);
        paymentRequestPublisher.publish(redisPaymentDto);
        return balanceAudit;
    }

    @Transactional
    @Scheduled(fixedRateString = "${scheduler.force-request.period}")
    public void approveRequestsForPayment(){
        List<BalanceAudit> pendingRequests = balanceAuditRepository.findSomeRequests(limit);
        pendingRequests.forEach(balanceAudit -> {
            balanceAudit.setPaymentStatus(PaymentStatus.SUCCESS);
            balanceAudit = balanceAuditRepository.save(balanceAudit);
            RedisPaymentDto redisPaymentDto = new RedisPaymentDto(balanceAudit.getSenderBalance().getId(),
                    balanceAudit.getGetterBalance().getId(), balanceAudit.getAmount(),
                    balanceAudit.getCurrency(), PaymentStatus.SUCCESS);
            paymentRequestPublisher.publish(redisPaymentDto);
        });
    }

    private Balance findBalance(Long id){
        return balanceRepository.findById(id)
                .orElseThrow(() -> new BalanceNotFoundException(id));
    }

    private Balance createBalance(Account account){
        return Balance.builder()
                .account(account)
                .authorizationBalance(new BigDecimal(0))
                .currentBalance(new BigDecimal(0))
                .created(ZonedDateTime.now())
                .updated(ZonedDateTime.now())
                .build();
    }
}