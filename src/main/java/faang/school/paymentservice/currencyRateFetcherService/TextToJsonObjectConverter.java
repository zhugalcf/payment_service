package faang.school.paymentservice.currencyRateFetcherService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.paymentservice.dto.CurrencyApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TextToJsonObjectConverter {
    private final ObjectMapper objectMapper;

    public CurrencyApiResponse convert(String text) throws JsonProcessingException {
            log.info("Text to be converted: " + text);
            CurrencyApiResponse convertedObject = objectMapper.readValue(text, CurrencyApiResponse.class);
            log.info("Converted object: " + convertedObject);
            return convertedObject;
    }
}
