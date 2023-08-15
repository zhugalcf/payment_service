package faang.school.paymentservice.service.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.paymentservice.client.OpenExchangeRatesClient;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CurrencyConverterServiceTest {
    @Mock
    private OpenExchangeRatesClient openExchangeRatesClient;
    @Mock
    private ObjectMapper objectMapper;

    private CurrencyConverterService currencyConverterService;
}