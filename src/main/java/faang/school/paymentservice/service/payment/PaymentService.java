package faang.school.paymentservice.service.payment;

import faang.school.paymentservice.dto.InvoiceDto;
import faang.school.paymentservice.dto.PaymentDto;
import faang.school.paymentservice.dto.PaymentStatus;
import faang.school.paymentservice.mapper.PaymentMapper;
import faang.school.paymentservice.model.Payment;
import faang.school.paymentservice.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    public PaymentDto create(InvoiceDto dto) {
        Optional<Payment> optionalPayment = getPaymentIfExist(dto);
        if (optionalPayment.isPresent()) {
            return paymentMapper.toDto(optionalPayment.get());
        }

        Payment payment = createPayment(dto);
        log.info("Created payment: {}", payment);

        return paymentMapper.toDto(payment);
    }

    public PaymentDto cancel(Long paymentId) {
        Payment payment = checkPaymentExist(paymentId);
        payment = cancelPayment(payment);
        log.info("Cancelled payment: {}", payment);

        return paymentMapper.toDto(payment);
    }

    public PaymentDto clear(Long paymentId) {
         Payment payment = checkPaymentExist(paymentId);

         payment = clearPayment(payment);
         log.info("Payment cleared: {}", payment);

         return paymentMapper.toDto(payment);
    }

    public void clear(Payment payment) {
        payment = checkPaymentExist(payment.getId());
        payment = clearPayment(payment);

        log.info("Payment cleared: {}", payment);
    }

    public PaymentDto schedule(Long paymentId, LocalDateTime scheduledAt) {
        Payment payment = checkPaymentExist(paymentId);
        payment = schedulePayment(payment, scheduledAt);

        return paymentMapper.toDto(payment);
    }

    public List<Payment> getScheduledPayment() {
        return paymentRepository.findAllScheduledPayments();
    }

    private Payment schedulePayment(Payment payment, LocalDateTime scheduledAt) {
        payment.setScheduledAt(scheduledAt);
        return paymentRepository.save(payment);
    }

    private Payment clearPayment(Payment payment) {
        payment.setStatus(PaymentStatus.CLEARED);
        return paymentRepository.save(payment);
    }

    private Payment checkPaymentExist(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment with id %d not found".formatted(paymentId)));
    }

    private Payment cancelPayment(Payment payment) {
        payment.setStatus(PaymentStatus.CANCELED);
        if (payment.getScheduledAt() != null) {
            payment.setScheduledAt(null);
        }
        return paymentRepository.save(payment);
    }

    private Payment createPayment(InvoiceDto dto) {
        Payment payment = Payment.builder()
                .senderAccount(dto.getSenderAccount())
                .receiverAccount(dto.getReceiverAccount())
                .currency(dto.getCurrency())
                .amount(dto.getAmount())
                .status(PaymentStatus.AUTHORIZATION)
                .idempotencyKey(dto.getIdempotencyKey())
                .build();
        return paymentRepository.save(payment);
    }

    private Optional<Payment> getPaymentIfExist(InvoiceDto dto) {
        return paymentRepository
                .findByIdempotencyKey(dto.getIdempotencyKey().toString());
    }
}
