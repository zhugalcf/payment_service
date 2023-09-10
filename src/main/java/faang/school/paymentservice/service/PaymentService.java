package faang.school.paymentservice.service;

import faang.school.paymentservice.dto.AccountDto;
import faang.school.paymentservice.dto.BalanceDto;
import faang.school.paymentservice.dto.UpdateBalanceDto;
import faang.school.paymentservice.exception.BalanceNotFoundException;
import faang.school.paymentservice.mapper.AccountMapper;
import faang.school.paymentservice.mapper.BalanceMapper;
import faang.school.paymentservice.model.Account;
import faang.school.paymentservice.model.Balance;
import faang.school.paymentservice.repository.AccountRepository;
import faang.school.paymentservice.repository.BalanceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final AccountRepository accountRepository;
    private final BalanceRepository balanceRepository;
    private final AccountMapper accountMapper;
    private final BalanceMapper balanceMapper;

    @Transactional
    public AccountDto createAccount(Long accountNumber){
        Account account = Account.builder().accountNumber(accountNumber).build();
        Account savedAccount = accountRepository.save(account);
        log.info(account.getId() + " is saved to db");

        Balance balance = Balance.builder()
                .account(savedAccount)
                .authorizationBalance(new BigDecimal(0))
                .currentBalance(new BigDecimal(0))
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .balanceVersion(0L)
                .build();
        balanceRepository.save(balance);
        log.info(balance.toString() + " is saved to db");

        return accountMapper.toDto(savedAccount);
    }

    public BalanceDto getBalance(Long id){
        Balance balance = balanceRepository.findById(id)
                .orElseThrow(() -> new BalanceNotFoundException(id));
        return balanceMapper.toDto(balance);
    }

    @Transactional
    public BalanceDto updateBalance(UpdateBalanceDto updateBalanceDto){
        Balance balance = balanceRepository.findById(updateBalanceDto.getBalanceId())
                .orElseThrow(() -> new BalanceNotFoundException(updateBalanceDto.getBalanceId()));

        BigDecimal currentBalance = balance.getCurrentBalance();
        BigDecimal deposit = updateBalanceDto.getDeposit();

        BigDecimal sum = currentBalance.add(deposit);

        balance.setBalanceVersion(balance.getBalanceVersion() + 1);
        balance.setCurrentBalance(sum);
        balance.setUpdated(LocalDateTime.now());
        balanceRepository.save(balance);

        log.info(balance + " is updated");

        return balanceMapper.toDto(balance);
    }
}
