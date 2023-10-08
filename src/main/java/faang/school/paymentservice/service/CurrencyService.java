package faang.school.paymentservice.service;

import faang.school.paymentservice.client.ExternalServiceClient;
import faang.school.paymentservice.dto.CurrencyApiResponse;
import faang.school.paymentservice.mapper.TextToJsonObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyService {
    private final ExternalServiceClient externalServiceClient;
    private final TextToJsonObjectMapper converter;
    private final Map<String, Double> currencyRates = new HashMap<>();

    @Retryable
    public CurrencyApiResponse fetchAndSaveCurrencyData() {
        String responseText = externalServiceClient.getLatestCurrencyRates();
        CurrencyApiResponse response = converter.convert(responseText, CurrencyApiResponse.class);

        Optional.ofNullable(response)
                .map(CurrencyApiResponse::getValute)
                .ifPresent(valute -> valute.forEach((currencyCode, currencyData) -> {
                    currencyRates.put(currencyData.getCharCode(), currencyData.getValue());
                    log.info("Currency rates fetched and updated.");
                }));
        if (currencyRates.isEmpty()) {
            log.error("Failed to fetch currency rates!");
        }
        return response;
    }

    @Recover
    public CurrencyApiResponse recover(Exception e) {
        return new CurrencyApiResponse(); // вернуть значение по умолчанию
    }
}