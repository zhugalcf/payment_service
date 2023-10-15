package faang.school.paymentservice.controller;

import faang.school.paymentservice.dto.Currency;
import faang.school.paymentservice.dto.PaymentDto;
import faang.school.paymentservice.dto.PaymentResponseDto;
import faang.school.paymentservice.dto.PaymentStatus;
import faang.school.paymentservice.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;
    @InjectMocks
    private PaymentController paymentController;
    private PaymentDto paymentDto;
    private PaymentResponseDto responseDto;
    private static final long PAYMENT_ID = 1L;
    private static final String UUID_TOKEN = "beb3e426-f4ec-4b60-85e6-85c519fe53d0";

    @Test
    void testCreatePayment() {
        paymentDto = PaymentDto.builder()
                .idempotencyKey(UUID.fromString(UUID_TOKEN))
                .ownerAccountNumber("123456789qwerty")
                .receiverAccountNumber("qwerty123456789")
                .amount(BigDecimal.valueOf(1000.0))
                .currency(Currency.RUB)
                .build();

        paymentController.createPayment(paymentDto);
        verify(paymentService).createPayment(paymentDto);
    }

    @Test
    void testRefundPayment() {
        paymentController.refundPayment(PAYMENT_ID);
        verify(paymentService).refundPayment(PAYMENT_ID);
    }

    @Test
    void testClearPayment() {
        paymentController.clearPayment(PAYMENT_ID);
        verify(paymentService).clearPayment(PAYMENT_ID);
    }

    @Test
    void testCheckPaymentStatus() {
        paymentController.checkPaymentStatus(PAYMENT_ID);
        verify(paymentService).checkPaymentStatus(PAYMENT_ID);
    }

    @Test
    void testHandlePaymentRequest() {
        responseDto = PaymentResponseDto.builder()
                .idempotencyKey(UUID.fromString(UUID_TOKEN))
                .status(PaymentStatus.SUCCESS)
                .build();

        paymentController.handlePaymentRequest(responseDto);
        verify(paymentService).handlePaymentRequest(responseDto);
    }
}
