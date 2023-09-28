package faang.school.paymentservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record CreatePaymentRequest(
        @NotNull
        long senderBalanceNumber,
        @NotNull
        long getterBalanceNumber,
        @Min(1)
        BigDecimal amount,
        @NotNull
        Currency currency,
        @NotNull
        ZonedDateTime clearScheduledAt
) {
}
