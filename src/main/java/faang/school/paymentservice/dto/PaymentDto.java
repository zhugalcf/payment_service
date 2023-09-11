package faang.school.paymentservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentDto {
    private Long id;
    private Long senderAccountId;
    private Long receiverAccountId;
    private Currency currency;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime scheduledAt;
    private LocalDateTime createdAt;
}
