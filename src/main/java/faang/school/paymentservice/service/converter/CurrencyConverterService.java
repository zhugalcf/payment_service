package faang.school.paymentservice.service.converter;

import faang.school.paymentservice.client.OpenExchangeRatesClient;
import faang.school.paymentservice.dto.Currency;
import faang.school.paymentservice.dto.OpenExchangeRatesResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyConverterService {
    private final OpenExchangeRatesClient openExchangeRatesClient;

    public ResponseEntity<OpenExchangeRatesResponseDto> convert(Enum<Currency> currentCurrency, Enum<Currency> targetCurrency, BigDecimal moneyAmount) {
        OpenExchangeRatesResponseDto latestExchangeRates =
                openExchangeRatesClient.getLatestExchangeRates();


        return ResponseEntity.accepted().body(latestExchangeRates);
    }
}
