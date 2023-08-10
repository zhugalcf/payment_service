package faang.school.paymentservice.dto;

import lombok.Data;

@Data
public class PaymentDto {
    private Long paymentId;
    private Double amount;
    private Currency currency;
}
