package faang.school.paymentservice.service;

import faang.school.paymentservice.config.TestContainerConfig;
import faang.school.paymentservice.model.AccountType;
import faang.school.paymentservice.repository.FreeAccountNumberRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@ContextConfiguration(classes = TestContainerConfig.class)
public class FreeAccountNumberServiceTest {
    @Autowired
    FreeAccountNumberService freeAccountNumbersService;
    @Autowired
    FreeAccountNumberRepository freeAccountNumberRepository;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:13.3"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void GenerateAccountNumberTest() {
        freeAccountNumbersService.getFreeNumber(AccountType.DEBIT, number -> System.out.println("The generated number is " +
                number));
        var number = freeAccountNumberRepository.getFreeAccountNumber(AccountType.DEBIT.getValue());
        System.out.println(number);
        Assertions.assertTrue(number.isPresent());
    }
}
