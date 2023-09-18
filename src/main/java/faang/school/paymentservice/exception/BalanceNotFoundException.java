package faang.school.paymentservice.exception;

import lombok.AllArgsConstructor;

import java.text.MessageFormat;

@AllArgsConstructor
public class BalanceNotFoundException extends RuntimeException{
    private Long id;

    @Override
    public String getMessage() {
        return MessageFormat.format("Balance {0} not found", id);
    }
}
