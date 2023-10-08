package faang.school.paymentservice.scheduler;

import faang.school.paymentservice.config.KafkaProducer;
import faang.school.paymentservice.model.OutboxPayment;
import faang.school.paymentservice.repository.OutboxPaymentRepository;
import faang.school.paymentservice.repository.PaymentRepository;
import faang.school.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledKafkaPoster {

    private final KafkaProducer kafkaProducer;
    private final OutboxPaymentRepository outboxRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    @Value("${spring.kafka.producer.topic}")
    private String paymentTopic;

    @Scheduled(cron = "${payment.scheduler.cron.kafka_poster}")
    @Transactional
    public void postPaymentToKafka() {
        log.info("Scheduled post_payment_to_kafka is starting");
        List<OutboxPayment> readyToPost = outboxRepository.findOutboxReadyToPost();
        for (OutboxPayment outbox: readyToPost) {
            kafkaProducer.sendPaymentOperational(paymentTopic, outbox.getStatus().name(), outbox.getIdempotencyKey());
        }
        log.info("Scheduled post_payment_to_kafka was executed");
    }

    @Scheduled(cron = "${payment.scheduler.cron.clear_payment}")
    @Transactional
    public void checkScheduledPayment() {
        log.info("Scheduled check_scheduled_payment is starting");
        List<Long> readyToClear = paymentRepository.findPaymentByClearScheduledAt();
        for (Long paymentId : readyToClear) {
            paymentService.clearPayment(paymentId);
        }
        log.info("Scheduled check_scheduled_payment was executed");
    }
}
