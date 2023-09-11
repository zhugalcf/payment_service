package faang.school.paymentservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InvoiceDto {

    @NotNull(message = "sender is required")
    private Long senderAccountId;

    @NotNull(message = "receiver is required")
    private Long receiverAccountId;

    @NotNull(message = "currency is required")
    private Currency currency;

    @NotNull(message = "amount is required")
    private BigDecimal amount;
}
