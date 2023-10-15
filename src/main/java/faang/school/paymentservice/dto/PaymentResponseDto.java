package faang.school.paymentservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponseDto {

    @NotNull(message = "UUID can't be null")
    private UUID idempotencyKey;
    @NotNull(message = "Status in response of payment can't be null")
    private PaymentStatus status;
}
