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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private BalanceRepository balanceRepository;
    @Spy
    private AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
    @Spy
    private BalanceMapper balanceMapper = Mappers.getMapper(BalanceMapper.class);
    @InjectMocks
    private PaymentService paymentService;

    private Balance balance;
    private Account account;
    private Long accountNumber;
    private UpdateBalanceDto updateBalanceDto;

    @BeforeEach
    void setUp(){
        accountNumber = 1L;
        account = Account.builder().id(accountNumber).accountNumber(accountNumber).build();
        ZonedDateTime now = ZonedDateTime.now();
        balance = createBalance(1L, account, now, new BigDecimal(0), new BigDecimal(0), 0L);

        updateBalanceDto = createUpdateDto(1L, new BigDecimal(300));
    }

    @Test
    void createAccount_Successful(){
        when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);
        when(balanceRepository.save(Mockito.any(Balance.class))).thenReturn(balance);

        AccountDto accountDto = paymentService.createAccount(accountNumber);

        assertEquals(accountNumber, accountDto.getAccountNumber());
    }

    @Test
    void getBalance_BalanceNotFoundException(){
        when(balanceRepository.findById(balance.getId())).thenReturn(Optional.empty());

        BalanceNotFoundException ex = assertThrows(
                BalanceNotFoundException.class, () -> paymentService.getBalance(balance.getId()));
        assertEquals(ex.getMessage(), MessageFormat.format("Balance {0} not found", balance.getId()));
    }

    @Test
    void getBalance_Successful(){
        when(balanceRepository.findById(balance.getId())).thenReturn(Optional.of(balance));
        BalanceDto expectedBalanceDto = balanceMapper.toDto(balance);

        BalanceDto actualBalanceDto = paymentService.getBalance(balance.getId());

        assertEquals(expectedBalanceDto, actualBalanceDto);
    }

    @Test
    void updateBalance_Successful(){
        lenient().when(balanceRepository.findById(balance.getId())).thenReturn(Optional.of(balance));
        when(balanceRepository.save(any())).thenReturn(balance);

        BalanceDto expectedBalanceDto = balanceMapper.toDto(balance);

        expectedBalanceDto.setCurrentBalance(updateBalanceDto.getDeposit());

        BalanceDto resultBalanceDto = paymentService.updateBalance(updateBalanceDto);

        assertEquals(expectedBalanceDto.getCurrentBalance(), resultBalanceDto.getCurrentBalance());
        assertEquals(expectedBalanceDto.getBalanceVersion() + 1, resultBalanceDto.getBalanceVersion());
    }

    @Test
    void updateBalance_NotFound(){
        when(balanceRepository.findById(balance.getId())).thenReturn(Optional.empty());
        lenient().when(balanceRepository.save(any())).thenReturn(balance);


        BalanceNotFoundException ex = assertThrows(
                BalanceNotFoundException.class, () -> paymentService.updateBalance(updateBalanceDto));
        assertEquals(ex.getMessage(), MessageFormat.format("Balance {0} not found", balance.getId()));
    }

    private Balance createBalance(Long id, Account account, ZonedDateTime time, BigDecimal authBalance, BigDecimal currentBlance,
                                  Long balanceVersion){
        return Balance.builder()
                .id(id)
                .account(account)
                .authorizationBalance(authBalance)
                .currentBalance(currentBlance)
                .created(time)
                .updated(time)
                .balanceVersion(balanceVersion)
                .build();
    }
    private UpdateBalanceDto createUpdateDto(Long balanceId, BigDecimal deposit){
        return UpdateBalanceDto.builder().balanceId(balanceId).deposit(deposit).build();
    }
}