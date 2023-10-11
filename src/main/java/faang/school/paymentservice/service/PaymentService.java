package faang.school.paymentservice.service;

import faang.school.paymentservice.client.AccountServiceClient;
import faang.school.paymentservice.dto.PaymentDto;
import faang.school.paymentservice.dto.PaymentResponseDto;
import faang.school.paymentservice.dto.PaymentStatus;
import faang.school.paymentservice.exception.BalanceException;
import faang.school.paymentservice.exception.CurrencyException;
import faang.school.paymentservice.exception.IdempotencyException;
import faang.school.paymentservice.exception.PaymentException;
import faang.school.paymentservice.mapper.PaymentMapper;
import faang.school.paymentservice.model.OutboxPayment;
import faang.school.paymentservice.model.Payment;
import faang.school.paymentservice.repository.OutboxPaymentRepository;
import faang.school.paymentservice.repository.PaymentRepository;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final AccountServiceClient accountService;
    private final PaymentRepository paymentRepository;
    private final OutboxPaymentRepository outboxPaymentRepository;
    private final PaymentMapper paymentMapper;

    @Value("${spring.kafka.producer.topic}")
    private String paymentTopic;
    @Value("${payment.scheduler.cron.seconds}")
    private Long scheduledSeconds;

    @Transactional
    public Long createPayment(PaymentDto paymentDto) {
        Payment payment;
        paymentDto.setStatus(PaymentStatus.UNPROCESSED);
        UUID idempotencyKey = paymentDto.getIdempotencyKey();

        Optional<Payment> paymentOptional = paymentRepository.findPaymentByIdempotencyKey(idempotencyKey);
        if (paymentOptional.isPresent()) {
            payment = paymentOptional.get();
            boolean isIdempotency = checkPaymentWithSameUUID(paymentDto, payment);
            if (!isIdempotency) {
                throw new IdempotencyException("This payment has already been made with other details! Try again!");
            }
            log.info("Payment with UUID={} is idempotency", idempotencyKey);
            return payment.getId();
        }

        paymentDto.setClearScheduledAt(LocalDateTime.now().plusSeconds(scheduledSeconds));
        paymentDto.setStatus(PaymentStatus.PROCESSING);
        payment = paymentRepository.save(paymentMapper.toEntity(paymentDto));
        log.info("Payment with UUID={} was saved in DB successfully", idempotencyKey);

        postRequestToAccountService(paymentDto);
        return payment.getId();
    }

    @Transactional
    public void refundPayment(long paymentId) {
        Payment payment = checkPaymentForRefundOrClear(paymentId);
        payment.setStatus(PaymentStatus.REFUND);
        OutboxPayment outbox = OutboxPayment.builder()
                .status(PaymentStatus.REFUND)
                .idempotencyKey(payment.getIdempotencyKey())
                .build();
        outboxPaymentRepository.save(outbox);
        log.info("Payment status with id={} has changed to {}", paymentId, payment.getStatus().name());
    }

    @Transactional
    public void clearPayment(long paymentId) {
        Payment payment = checkPaymentForRefundOrClear(paymentId);
        payment.setStatus(PaymentStatus.CLEAR);
        OutboxPayment outbox = OutboxPayment.builder()
                .status(PaymentStatus.CLEAR)
                .idempotencyKey(payment.getIdempotencyKey())
                .build();
        outboxPaymentRepository.save(outbox);
        log.info("Payment status with id={} has changed to {}", paymentId, payment.getStatus().name());
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public String checkPaymentStatus(long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new EntityNotFoundException("This payment doesn't exist"));
        return String.format("Payment with UUID = %s has been created at %s and has status = %s",
                payment.getIdempotencyKey(), payment.getCreatedAt(), payment.getStatus());
    }

    @Transactional
    public void handlePaymentRequest(PaymentResponseDto responseDto) {
        UUID idempotencyKey = responseDto.getIdempotencyKey();

        Payment payment = paymentRepository.findPaymentByIdempotencyKey(idempotencyKey)
                .orElseThrow(() -> new EntityNotFoundException("Payment with this token can't exist"));
        OutboxPayment outbox = outboxPaymentRepository.findOutboxPaymentByIdempotencyKey(idempotencyKey).orElseThrow(
                () -> new EntityNotFoundException("Actual payment doesn't exist"));

        outbox.setPosted(true);
        payment.setStatus(responseDto.getStatus());
        log.info("Payment status with id={} has changed to {}", payment.getId(), payment.getStatus().name());
    }

    private void postRequestToAccountService(PaymentDto paymentDto) {
        try {
            accountService.createPayment(paymentDto);
            log.info("New payment, UUID={}, has been posted to account-service", paymentDto.getIdempotencyKey());
        } catch (FeignException e) {
            int exceptionStatus = e.status();
            if (exceptionStatus == 400) {
                throw new CurrencyException("Wrong currency");
            } else if (exceptionStatus == 403) {
                throw new BalanceException("There are not enough money in this account balance");
            } else {
                throw e;
            }
        }
    }

    private Payment checkPaymentForRefundOrClear(long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new EntityNotFoundException("This payment doesn't exist"));
        if (payment.getStatus() != PaymentStatus.PROCESSING) {
            throw new PaymentException("It is impossible to perform this operation with this payment, since it has already been closed");
        }
        return payment;
    }

    private boolean checkPaymentWithSameUUID(PaymentDto newPayment, Payment oldPayment) {
        if (newPayment.getOwnerAccountNumber().equals(oldPayment.getOwnerAccountNumber())
                && newPayment.getReceiverAccountNumber().equals(oldPayment.getReceiverAccountNumber())
                && newPayment.getAmount().compareTo(oldPayment.getAmount()) == 0
                && newPayment.getCurrency() == oldPayment.getCurrency()) {
            return true;
        }
        return false;
    }
}
