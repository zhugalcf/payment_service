package faang.school.paymentservice.service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.paymentservice.client.OpenExchangeRatesClient;
import faang.school.paymentservice.dto.Currency;
import faang.school.paymentservice.dto.OpenExchangeRatesResponseDto;
import faang.school.paymentservice.exception.CurrencyNotSupportedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyConverterService {
    private final OpenExchangeRatesClient openExchangeRatesClient;
    private final ObjectMapper objectMapper;
    private static final double COMMISSION_RATE = 1.01;

    public BigDecimal convertCurrencyWithCommission(Currency currentCurrency, Currency targetCurrency, BigDecimal moneyAmount) throws JsonProcessingException {
        String json = openExchangeRatesClient.getLatestExchangeRates();
        OpenExchangeRatesResponseDto ratesApiResponse = objectMapper.readValue(json, OpenExchangeRatesResponseDto.class);

        if (!ratesApiResponse.getRates().containsKey(currentCurrency.toString()) || !ratesApiResponse.getRates().containsKey(targetCurrency.toString())) {
            throw new CurrencyNotSupportedException(currentCurrency + " is not supported");
        }
        BigDecimal targetCurrencyRate = ratesApiResponse.getRates().get(targetCurrency.toString());


        return targetCurrencyRate.multiply(moneyAmount).multiply(BigDecimal.valueOf(COMMISSION_RATE));
    }
}
