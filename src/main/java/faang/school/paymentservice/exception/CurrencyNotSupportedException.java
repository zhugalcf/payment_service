package faang.school.paymentservice.exception;

public class CurrencyNotSupportedException extends RuntimeException{
    public CurrencyNotSupportedException(String message) {
        super(message);
    }
}
