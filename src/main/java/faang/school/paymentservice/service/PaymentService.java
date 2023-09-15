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
import java.time.ZonedDateTime;

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
        log.info("Account {} is saved to db", account.getId());

        Balance balance = balanceRepository.save(createBalance(savedAccount));
        log.info("Balance {} is saved to db", balance);

        return accountMapper.toDto(savedAccount);
    }

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