package faang.school.paymentservice.exchangeRates;

import lombok.Data;

@Data
public class CurrencyData {
    private String currencyCode;
    private double exchangeRate;
}
