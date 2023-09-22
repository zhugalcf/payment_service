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
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            throw new EntityNotFoundException("Payment with id %d not found".formatted(paymentId));
        }
        Payment payment = cancelPayment(optionalPayment.get());
        log.info("Cancelled payment: {}", payment);

        return paymentMapper.toDto(payment);
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
