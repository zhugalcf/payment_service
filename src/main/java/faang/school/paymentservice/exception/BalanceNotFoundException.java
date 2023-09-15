package faang.school.paymentservice.exception;

import java.text.MessageFormat;

public class BalanceNotFoundException extends RuntimeException{
    private Long id;

    public BalanceNotFoundException(Long id){
        this.id = id;
    }

    @Override
    public String getMessage() {
        return MessageFormat.format("Balance {0} not found", id);
    }
}
