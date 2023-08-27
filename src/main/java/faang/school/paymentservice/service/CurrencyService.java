package faang.school.paymentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import faang.school.paymentservice.client.ExchangeRateClient;
import faang.school.paymentservice.dto.exchange.CurrencyApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyService {

    private final ExchangeRateClient exchangeRateClient;
    private final TextToJsonObjectConverter textToJsonObjectConverter;
    private final Map<String, BigDecimal> currencyRates = new HashMap<>();

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public CurrencyApiResponse getCurrencyRate() {
        String currencyRate = exchangeRateClient.getLatestExchangeRates();
        CurrencyApiResponse response;

        try {
            response = textToJsonObjectConverter.convert(currencyRate, CurrencyApiResponse.class);
        }catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Optional.ofNullable(response)
                .map(CurrencyApiResponse::getValute)
                .ifPresent(valute -> valute.forEach((currencyCode, currencyData) -> {
                    currencyRates.put(currencyData.getCharCode(), currencyData.getValue());
                    currencyRates.forEach((key, value) -> {
                        System.out.println(key + " : " + value);
                        log.info("Key: {}, Value: {}", key, value);
                    });
                }));
        if (currencyRates.isEmpty()) {
            log.info("Currency rates not found");
        }
        return response;
    }

}
