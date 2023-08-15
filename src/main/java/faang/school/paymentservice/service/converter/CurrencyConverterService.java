package faang.school.paymentservice.service.converter;

import faang.school.paymentservice.client.OpenExchangeRatesClient;
import faang.school.paymentservice.dto.Currency;
import faang.school.paymentservice.dto.OpenExchangeRatesResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyConverterService {
    private final OpenExchangeRatesClient openExchangeRatesClient;

    public ResponseEntity<OpenExchangeRatesResponseDto> convert(Enum<Currency> currentCurrency, Enum<Currency> targetCurrency, BigDecimal moneyAmount) {
            OpenExchangeRatesResponseDto response = openExchangeRatesClient.getLatestExchangeRates();

            Map<Currency, BigDecimal> rates = response.getRates().stream()
                    .filter(rate -> rate.containsKey(currentCurrency) && rate.containsKey(targetCurrency))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Exchange rate data not available for the specified currency pair"));

            return ResponseEntity.ok(response);
    }
}
