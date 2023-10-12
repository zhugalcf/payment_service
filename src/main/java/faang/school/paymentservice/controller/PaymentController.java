package faang.school.paymentservice.controller;

import faang.school.paymentservice.dto.PaymentDto;
import faang.school.paymentservice.dto.PaymentResponseDto;
import faang.school.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping()
    public Long createPayment(@RequestBody @Validated PaymentDto paymentDto) {
        log.info("Received request to create payment: idempotencyToken={}, receiverAccount={}, senderAccount={}",
                paymentDto.getIdempotencyKey(), paymentDto.getReceiverAccountNumber(), paymentDto.getOwnerAccountNumber());
        return paymentService.createPayment(paymentDto);
    }

    @PutMapping("/{id}/refund")
    public void refundPayment(@PathVariable("id") long paymentId) {
        log.info("Received request to refund payment with id={}", paymentId);
        paymentService.refundPayment(paymentId);
    }

    @PutMapping("/{id}/clear")
    public void clearPayment(@PathVariable("id") long paymentId) {
        log.info("Received request to clear payment with id={}", paymentId);
        paymentService.clearPayment(paymentId);
    }

    @GetMapping("/{id}/status")
    public PaymentDto checkPaymentStatus(@PathVariable("id") long paymentId) {
        log.info("Received request to check status of payment with id={}", paymentId);
        return paymentService.checkPaymentStatus(paymentId);
    }

    @PostMapping("/handle/answer")
    public void handlePaymentRequest(@RequestBody @Validated PaymentResponseDto responseDto) {
        log.info("Received request with answer of payment: idempotencyToken={}", responseDto.getIdempotencyKey());
        paymentService.handlePaymentRequest(responseDto);
    }
}
