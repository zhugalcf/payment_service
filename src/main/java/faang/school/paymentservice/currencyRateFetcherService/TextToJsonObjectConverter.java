package faang.school.paymentservice.currencyRateFetcherService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TextToJsonObjectConverter {
    private final ObjectMapper objectMapper;

    public <T> T convert(String text, Class<T> targetType) throws JsonProcessingException {
            System.out.println("Text to be converted: " + text);
            T convertedObject = objectMapper.readValue(text, targetType);
            System.out.println("Converted object: " + convertedObject);
            return convertedObject;
    }
}
