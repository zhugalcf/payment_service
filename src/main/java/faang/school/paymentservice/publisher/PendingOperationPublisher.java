package faang.school.paymentservice.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.paymentservice.dto.PendingOperationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class PendingOperationPublisher extends AbstractPublisher<PendingOperationDto> {

    public PendingOperationPublisher(RedisTemplate<String, Object> redisTemplate,
                                     ObjectMapper objectMapper,
                                     @Value("${spring.data.redis.channels.pending_operation_channel.name}") String channel) {
        super(redisTemplate, objectMapper, channel);
    }
}
