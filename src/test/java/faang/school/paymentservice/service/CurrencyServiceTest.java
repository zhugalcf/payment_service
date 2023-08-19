package faang.school.paymentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import faang.school.paymentservice.client.ExchangeRateClient;
import faang.school.paymentservice.dto.exchange.CurrencyApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {
    @Mock
    private ExchangeRateClient exchangeRateClient;
    @Mock
    private TextToJsonObjectConverter converter;
    @InjectMocks
    private CurrencyService currencyService;

    @Test
    void getCurrencyRate() throws JsonProcessingException {

        String updatedDate = "2023-08-14T11:30:00+03:00";
        String responseText = """
                {
                    "Date": "%s", // Use the updated date here
                    "PreviousDate": "2023-08-11T11:30:00+03:00",
                    "PreviousURL": "//www.cbr-xml-daily.ru/archive/2023/08/11/daily_json.js",
                    "Timestamp": "2023-08-14T10:00:00.000+00:00",
                    "Valute": {
                        "USD": {
                            "CharCode": "USD",
                            "Value": 100
                        }
                    }
                }""".formatted(updatedDate);


        doReturn(responseText).when(exchangeRateClient).getLatestExchangeRates();
        doReturn(new CurrencyApiResponse()).when(converter).convert(responseText, CurrencyApiResponse.class);

        CurrencyApiResponse response = currencyService.getCurrencyRate();

        assertNotNull(response);
    }
}