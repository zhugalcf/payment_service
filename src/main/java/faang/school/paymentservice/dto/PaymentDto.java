package faang.school.paymentservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentDto {
    private Long paymentId;
    private Double amount;
    @NotNull(message = "There is no currency to convert!")
    private String currency;
}
