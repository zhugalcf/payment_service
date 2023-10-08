package faang.school.paymentservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {

    private Long paymentId;
    @NotNull
    @Size(min = 12, max = 20, message = "Length of owner's number must be more then 12 and less then 20")
    private String ownerAccountNumber;
    @NotNull
    @Size(min = 12, max = 20, message = "Length of receiver's number must be more then 12 and less then 20")
    private String receiverAccountNumber;
    private PaymentStatus status;
    @NotNull(message = "Amount of payment can't be empty")
    private BigDecimal amount;
    @NotNull(message = "Unknown currency")
    private Currency currency;
    @NotNull(message = "UUID can't be null")
    private UUID idempotencyKey;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime clearScheduledAt;
}
