package faang.school.paymentservice.dto;

import lombok.Data;

import java.util.Map;

@Data
public class CurrencyData {
    private String currency;
    private Double exchangeRate;
    private Map<String, Double> currencyMap;
}
