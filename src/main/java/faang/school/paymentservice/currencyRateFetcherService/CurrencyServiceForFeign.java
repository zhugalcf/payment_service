package faang.school.paymentservice.currencyRateFetcherService;

import com.fasterxml.jackson.core.JsonProcessingException;
import faang.school.paymentservice.dto.CurrencyApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyServiceForFeign {
    private final ExternalServiceClient externalServiceClient;
    private final TextToJsonObjectConverter converter;
    //private final Map<String, Double> currencyRates = new HashMap<>(); //валюта - курс
    private final Logger logger = LoggerFactory.getLogger(CurrencyServiceForFeign.class);

    @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 1000)) // повторить до 2-х раз и с задержкой 1 сек.
    public CurrencyApiResponse fetchAndSaveCurrencyData() {
        String responseText = externalServiceClient.getLatestCurrencyRates();
        CurrencyApiResponse response = null;
        try {
            response = converter.convert(responseText, CurrencyApiResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Map<String, Double> currencyRates = new HashMap<>();

        Optional.ofNullable(response)
                .map(CurrencyApiResponse::getValute)
                .ifPresent(valute -> {
                    currencyRates.clear();
                    valute.forEach((currencyCode, currencyData) -> {
                        currencyRates.put(currencyData.getCharCode(), currencyData.getValue());
                        logger.info("Currency rates fetched and updated.");
                    });
                });
        if (currencyRates.isEmpty()) {
            logger.warn("Failed to fetch currency rates!");
        }
        return response;
    }

    @Recover
    public CurrencyApiResponse recover(Exception e) {
        return new CurrencyApiResponse(); // вернуть значение по умолчанию
    }
}
