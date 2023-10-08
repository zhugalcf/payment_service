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
        log.info("Endpoint <createPayment>, uri='/payment' was called successfully");
        return paymentService.createPayment(paymentDto);
    }

    @PutMapping("/{id}/refund")
    public void refundPayment(@PathVariable("id") long paymentId) {
        log.info("Endpoint <refundPayment>, uri=/payment/{}/refund was called successfully", paymentId);
        paymentService.refundPayment(paymentId);
    }

    @PutMapping("/{id}/clear")
    public void clearPayment(@PathVariable("id") long paymentId) {
        log.info("Endpoint <clearPayment>, uri=/payment/{}/clear was called successfully", paymentId);
        paymentService.clearPayment(paymentId);
    }

    @GetMapping("/{id}/status")
    public String checkPaymentStatus(@PathVariable("id") long paymentId) {
        log.info("Endpoint <checkPaymentStatus>, uri=/payment/{}/status was called successfully", paymentId);
        return paymentService.checkPaymentStatus(paymentId);
    }

    @PostMapping("/handle/answer")
    public void handlePaymentRequest(@RequestBody @Validated PaymentResponseDto responseDto) {
        log.info("Endpoint <handlePaymentRequest>, uri=/payment/handle/answer for payment={} was called successfully",
                responseDto.getIdempotencyKey());
        paymentService.handlePaymentRequest(responseDto);
    }
}
