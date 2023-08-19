package faang.school.paymentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TextToJsonObjectConverter {
    private final ObjectMapper objectMapper;

    public <T> T convert(String text, Class<T> targetType) throws JsonProcessingException {
        log.info("Text to be converted: {}", text);
        T convertedObject = objectMapper.readValue(text, targetType);
        log.info("Converted object: {}", convertedObject);
        return convertedObject;
    }
}
