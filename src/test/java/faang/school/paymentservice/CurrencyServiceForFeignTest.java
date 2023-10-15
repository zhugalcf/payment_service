package faang.school.paymentservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import faang.school.paymentservice.service.CurrencyService;
import faang.school.paymentservice.client.ExternalServiceClient;
import faang.school.paymentservice.mapper.TextToJsonObjectMapper;
import faang.school.paymentservice.dto.CurrencyApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceForFeignTest {

    @Mock
    private ExternalServiceClient externalServiceClient;

    @Mock
    private TextToJsonObjectMapper textToJsonObjectConverter;

    @InjectMocks
    private CurrencyService currencyServiceForFeign;

    @Test
    public void fetchAndSaveCurrencyDataTest() throws JsonProcessingException {
        String responseText = """
                {
                    "Date": "2023-08-12T11:30:00+03:00",
                    "PreviousDate": "2023-08-11T11:30:00+03:00",
                    "PreviousURL": "//www.cbr-xml-daily.ru/archive/2023/08/11/daily_json.js",
                    "Timestamp": "2023-08-14T10:00:00.000+00:00",
                    "Valute": {
                        "AUD": {
                            "CharCode": "AUD",
                            "Value": 64.0503
                        },
                        }
                    }
                }""";

        doReturn(responseText).when(externalServiceClient).getLatestCurrencyRates();
        doReturn(new CurrencyApiResponse()).when(textToJsonObjectConverter).convert(responseText, CurrencyApiResponse.class);

        CurrencyApiResponse response = currencyServiceForFeign.fetchAndSaveCurrencyData();

        assertNotNull(response);
    }
}