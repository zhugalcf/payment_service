package faang.school.paymentservice.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TextToJsonObjectMapper {
    private final ObjectMapper objectMapper;

    public <T> T convert(String text, Class<T> targetType) {
        try {
            log.info("Text to be converted: " + text);
            return objectMapper.readValue(text, targetType);
        } catch (JsonProcessingException e) {
            log.error("Error while converting response text to object: " + e.getMessage());
            return null;
        }
    }
}
