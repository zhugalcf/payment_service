package faang.school.paymentservice.service;

import faang.school.paymentservice.client.AccountServiceClient;
import faang.school.paymentservice.dto.Currency;
import faang.school.paymentservice.dto.PaymentDto;
import faang.school.paymentservice.dto.PaymentResponseDto;
import faang.school.paymentservice.dto.PaymentStatus;
import faang.school.paymentservice.exception.IdempotencyException;
import faang.school.paymentservice.exception.PaymentException;
import faang.school.paymentservice.mapper.PaymentMapperImpl;
import faang.school.paymentservice.model.OutboxPayment;
import faang.school.paymentservice.model.Payment;
import faang.school.paymentservice.repository.OutboxPaymentRepository;
import faang.school.paymentservice.repository.PaymentRepository;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OutboxPaymentRepository outboxRepository;
    @Mock
    private AccountServiceClient accountServiceClient;
    @Spy
    private PaymentMapperImpl paymentMapper;
    @InjectMocks
    private PaymentService paymentService;

    private PaymentDto paymentDto;
    private Payment payment;
    private PaymentResponseDto responseDto;
    private static final UUID UUID_TOKEN = java.util.UUID.fromString("0bcd92c9-2247-49ef-a3e8-1da83a482579");
    private static final BigDecimal AMOUNT = new BigDecimal(5000.0);
    private static final Long PAYMENT_ID = 1L;

    @BeforeEach
    void initData() {
        paymentDto = PaymentDto.builder()
                .idempotencyKey(UUID_TOKEN)
                .ownerAccountNumber("123456789qwerty")
                .receiverAccountNumber("qwerty123456789")
                .amount(AMOUNT)
                .currency(Currency.RUB)
                .build();
        payment = Payment.builder()
                .id(PAYMENT_ID)
                .idempotencyKey(UUID_TOKEN)
                .ownerAccountNumber("123456789qwerty")
                .receiverAccountNumber("qwerty123456789")
                .amount(AMOUNT)
                .currency(Currency.RUB)
                .status(PaymentStatus.PROCESSING)
                .build();
        responseDto = PaymentResponseDto.builder()
                .idempotencyKey(UUID_TOKEN)
                .status(PaymentStatus.SUCCESS)
                .build();
        ReflectionTestUtils.setField(paymentService, "scheduledSeconds", 28800L);
    }


    @Test
    void testCreatePayment() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        Long actualPaymentId = paymentService.createPayment(paymentDto);
        assertEquals(PAYMENT_ID, actualPaymentId);
    }

    @Test
    void testCreatePaymentWithIdempotencyOperational() {
        when(paymentRepository.findPaymentByIdempotencyKey(UUID_TOKEN)).thenReturn(Optional.ofNullable(payment));
        Long actualId = paymentService.createPayment(paymentDto);
        assertEquals(PAYMENT_ID, actualId);
    }

    @Test
    void testCreatePaymentWithNoIdempotencyOperational() {
        payment.setCurrency(Currency.USD);
        when(paymentRepository.findPaymentByIdempotencyKey(UUID_TOKEN)).thenReturn(Optional.ofNullable(payment));
        assertThrows(IdempotencyException.class, () -> paymentService.createPayment(paymentDto));
    }

    @Test
    void testCreatePaymentWithFeignException() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(accountServiceClient.createPayment(paymentDto)).thenThrow(FeignException.class);
        assertThrows(FeignException.class, () -> paymentService.createPayment(paymentDto));
    }

    @Test
    void testRefundPayment() {
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.ofNullable(payment));
        paymentService.refundPayment(PAYMENT_ID);

        PaymentStatus actualStatus = payment.getStatus();
        assertEquals(PaymentStatus.REFUND, actualStatus);
        verify(outboxRepository).save(any(OutboxPayment.class));
    }

    @Test
    void testRefundPaymentWithNonExistPayment() {
        when(paymentRepository.findById(PAYMENT_ID)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> paymentService.refundPayment(PAYMENT_ID));
    }

    @Test
    void testRefundPaymentWithAlreadyClosedPayment() {
        payment.setStatus(PaymentStatus.REFUND);
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.ofNullable(payment));
        assertThrows(PaymentException.class, () -> paymentService.refundPayment(PAYMENT_ID));
    }

    @Test
    void testClearPayment() {
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.ofNullable(payment));
        paymentService.clearPayment(PAYMENT_ID);

        PaymentStatus actualStatus = payment.getStatus();
        assertEquals(PaymentStatus.CLEAR, actualStatus);
        verify(outboxRepository).save(any(OutboxPayment.class));
    }

    @Test
    void testClearPaymentWithNonExistPayment() {
        when(paymentRepository.findById(PAYMENT_ID)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> paymentService.clearPayment(PAYMENT_ID));
    }

    @Test
    void testClearPaymentWithAlreadyClearPayment() {
        payment.setStatus(PaymentStatus.CLEAR);
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.ofNullable(payment));
        assertThrows(PaymentException.class, () -> paymentService.clearPayment(PAYMENT_ID));
    }

    @Test
    void testCheckPaymentStatus(){
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.ofNullable(payment));
        String expected = String.format("Payment with UUID = %s has been created at %s and has status = %s",
                UUID_TOKEN, null, PaymentStatus.PROCESSING);
        String actual = paymentService.checkPaymentStatus(PAYMENT_ID);
        assertEquals(expected, actual);
    }

    @Test
    void testCheckPaymentStatusWithNonExistPayment() {
        when(paymentRepository.findById(PAYMENT_ID)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> paymentService.checkPaymentStatus(PAYMENT_ID));
    }

    @Test
    void testHandlePaymentRequest() {
        OutboxPayment outbox = OutboxPayment.builder()
                .idempotencyKey(UUID_TOKEN)
                .status(PaymentStatus.PROCESSING)
                .build();
        when(paymentRepository.findPaymentByIdempotencyKey(UUID_TOKEN)).thenReturn(Optional.ofNullable(payment));
        when(outboxRepository.findOutboxPaymentByIdempotencyKey(UUID_TOKEN)).thenReturn(Optional.ofNullable(outbox));

        paymentService.handlePaymentRequest(responseDto);

        PaymentStatus actualStatus = payment.getStatus();
        boolean actualPosted = outbox.isPosted();
        assertEquals(PaymentStatus.SUCCESS, actualStatus);
        assertEquals(true, actualPosted);
    }

    @Test
    void testHandlePaymentRequestWithNonExistOutbox() {
        when(paymentRepository.findPaymentByIdempotencyKey(UUID_TOKEN)).thenReturn(Optional.ofNullable(payment));
        when(outboxRepository.findOutboxPaymentByIdempotencyKey(UUID_TOKEN)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> paymentService.handlePaymentRequest(responseDto));
    }

    @Test
    void testHandlePaymentRequestWithNonExistPayment() {
        when(paymentRepository.findPaymentByIdempotencyKey(UUID_TOKEN)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> paymentService.handlePaymentRequest(responseDto));
    }
}
