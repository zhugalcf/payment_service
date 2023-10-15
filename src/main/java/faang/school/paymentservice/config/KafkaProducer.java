package faang.school.paymentservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Async("kafkaThreadPool")
    public void sendMessage(String topicName, Object value) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, value);
        handleFuture(future);
    }

    @Async("kafkaThreadPool")
    public void sendPaymentOperational(String topicName, String operational, UUID idempotencyKey) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicName, operational, idempotencyKey);
        handleFuture(future);
    }

    private void handleFuture(CompletableFuture<SendResult<String, Object>> future) {
        future.whenComplete(((sendResult, throwable) -> {
            String key = sendResult.getProducerRecord().key();
            Object value = sendResult.getProducerRecord().value();
            if (throwable != null) {
                handleFailure(key, value, throwable);
            } else {
                handleSuccess(key, value, sendResult);
            }
        }));
    }

    private void handleSuccess(String key, Object value, SendResult<String, Object> sendResult) {
        log.info("Message sent successfully for the key: {} and the value: {}, partition is: {}",
                key, value, sendResult.getRecordMetadata().partition());
    }

    private void handleFailure(String key, Object value, Throwable throwable) {
        log.error("Error sending message and exception is {}", throwable.getMessage(), throwable);
    }
}
