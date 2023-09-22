package faang.school.paymentservice.service.payment;

import faang.school.paymentservice.dto.InvoiceDto;
import faang.school.paymentservice.dto.PaymentDto;
import faang.school.paymentservice.dto.PaymentStatus;
import faang.school.paymentservice.mapper.PaymentMapper;
import faang.school.paymentservice.model.Payment;
import faang.school.paymentservice.repository.PaymentRepository;
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
        paymentRepository.save(payment);
        log.info("Created payment: {}", payment);

        return paymentMapper.toDto(payment);
    }

    private Payment createPayment(InvoiceDto dto) {
        return Payment.builder()
                .senderAccount(dto.getSenderAccount())
                .receiverAccount(dto.getReceiverAccount())
                .currency(dto.getCurrency())
                .amount(dto.getAmount())
                .status(PaymentStatus.AUTHORIZATION)
                .idempotencyKey(dto.getIdempotencyKey())
                .build();
    }

    private Optional<Payment> getPaymentIfExist(InvoiceDto dto) {
        return paymentRepository
                .findByIdempotencyKey(dto.getIdempotencyKey().toString());
    }
}
