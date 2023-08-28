package faang.school.paymentservice.service.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.paymentservice.client.OpenExchangeRatesClient;
import faang.school.paymentservice.dto.Currency;
import faang.school.paymentservice.dto.OpenExchangeRatesResponseDto;
import faang.school.paymentservice.exception.CurrencyNotSupportedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyConverterServiceTest {
    @Mock
    private OpenExchangeRatesClient openExchangeRatesClient;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private CurrencyConverterService currencyConverterService;

    @Test
    void testConvertCurrencyWithCommission_CurrencyNotSupported() throws Exception {
        String jsonResponse = "{ \"rates\": { \"USD\": 1.2, \"EUR\": 0.9 } }";
        when(openExchangeRatesClient.getLatestExchangeRates()).thenReturn(jsonResponse);

        OpenExchangeRatesResponseDto responseDto = new OpenExchangeRatesResponseDto();
        responseDto.setRates(Map.of("USD", new BigDecimal("1.2"), "EUR", new BigDecimal("0.9")));
        when(objectMapper.readValue(jsonResponse, OpenExchangeRatesResponseDto.class)).thenReturn(responseDto);

        Currency currentCurrency = Currency.USD;
        Currency unsupportedCurrency = Currency.RUB;
        BigDecimal moneyAmount = BigDecimal.valueOf(100);

        CurrencyNotSupportedException currencyNotSupportedException = assertThrows(CurrencyNotSupportedException.class, () -> {
            currencyConverterService.convertCurrencyWithCommission(currentCurrency, unsupportedCurrency, moneyAmount);
        });

        assertEquals(currencyNotSupportedException.getMessage(), currentCurrency + " is not supported");
    }

}