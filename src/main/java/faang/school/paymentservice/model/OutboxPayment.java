package faang.school.paymentservice.model;

import faang.school.paymentservice.dto.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "outbox_payment")
public class OutboxPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "idempotencyKey", nullable = false)
    private UUID idempotencyKey;

    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "is_posted", nullable = false)
    private boolean isPosted;
}
