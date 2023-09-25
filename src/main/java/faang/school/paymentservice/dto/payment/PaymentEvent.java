package faang.school.paymentservice.dto.payment;

import faang.school.paymentservice.dto.Currency;
import faang.school.paymentservice.dto.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentEvent {
    private String senderAccount;
    private String receiverAccount;
    private Currency currency;
    private BigDecimal amount;
    private PaymentStatus type;
}
