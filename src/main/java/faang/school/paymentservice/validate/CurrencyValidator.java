package faang.school.paymentservice.validate;

import faang.school.paymentservice.dto.PaymentDto;
import org.springframework.stereotype.Component;

@Component
public class CurrencyValidator {
    public void validateCurrency(PaymentDto paymentDto) {
        if (paymentDto.getCurrency() == null) {
            throw new IllegalArgumentException("There is no currency to convert!");
        }
    }
}
