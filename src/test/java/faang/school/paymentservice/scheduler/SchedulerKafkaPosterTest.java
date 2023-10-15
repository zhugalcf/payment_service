package faang.school.paymentservice.scheduler;

import faang.school.paymentservice.config.KafkaProducer;
import faang.school.paymentservice.dto.PaymentStatus;
import faang.school.paymentservice.model.OutboxPayment;
import faang.school.paymentservice.repository.OutboxPaymentRepository;
import faang.school.paymentservice.repository.PaymentRepository;
import faang.school.paymentservice.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SchedulerKafkaPosterTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OutboxPaymentRepository outboxRepository;
    @Mock
    private KafkaProducer kafkaProducer;
    @Mock
    private PaymentService paymentService;
    @InjectMocks
    private ScheduledKafkaPoster kafkaPoster;

    private List<OutboxPayment> readyToPostPayments;
    private String paymentTopic;
    private final UUID UUID_TOKEN_1 = UUID.fromString("bedd195f-87e8-4cd2-b585-d6b0de2ae684");
    private final UUID UUID_TOKEN_2 = UUID.fromString("f8332d80-7c56-4bb1-8e70-34216c0c77cd");
    private final String ACTUAL_PAYMENT_TOPIC = "payment_transaction_DMS";

    @BeforeEach
    void initData() {
        readyToPostPayments = List.of(OutboxPayment.builder()
                        .status(PaymentStatus.CLEAR)
                        .idempotencyKey(UUID_TOKEN_1)
                .build(), OutboxPayment.builder()
                        .status(PaymentStatus.REFUND)
                        .idempotencyKey(UUID_TOKEN_2)
                .build());
        ReflectionTestUtils.setField(kafkaPoster, "paymentTopic", "payment_transaction_DMS");
    }

    @Test
    void testPostPaymentToKafka() {
        when(outboxRepository.findOutboxReadyToPost()).thenReturn(readyToPostPayments);
        kafkaPoster.postPaymentToKafka();
        verify(kafkaProducer).sendPaymentOperational(ACTUAL_PAYMENT_TOPIC, "REFUND", UUID_TOKEN_2);
        verify(kafkaProducer).sendPaymentOperational(ACTUAL_PAYMENT_TOPIC, "CLEAR", UUID_TOKEN_1);
    }

    @Test
    void testCheckScheduledPayment() {
        when(paymentRepository.findPaymentByClearScheduledAt()).thenReturn(List.of(1L, 2L, 3L));
        kafkaPoster.checkScheduledPayment();

        verify(paymentService).clearPayment(1L);
        verify(paymentService).clearPayment(2L);
        verify(paymentService).clearPayment(3L);
    }
}
