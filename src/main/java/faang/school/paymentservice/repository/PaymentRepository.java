package faang.school.paymentservice.repository;

import faang.school.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.idempotencyKey = :idempotencyKey")
    Optional<Payment> findPaymentByIdempotencyKey(@Param("idempotencyKey") UUID idempotencyKey);

    @Query("SELECT p.id FROM Payment p WHERE p.clearScheduledAt <= NOW()")
    List<Long> findPaymentByClearScheduledAt();
}
