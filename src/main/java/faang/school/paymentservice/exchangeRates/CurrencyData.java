package faang.school.paymentservice.exchangeRates;

import lombok.Data;

@Data
public class CurrencyData {
    private String currencyCode; //сокр. название валют
    private double exchangeRate; //сам курс валюты
}
