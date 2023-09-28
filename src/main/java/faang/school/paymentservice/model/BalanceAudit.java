package faang.school.paymentservice.model;

import faang.school.paymentservice.dto.Currency;
import faang.school.paymentservice.dto.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="balance_audit")
public class BalanceAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "sender_balance_id", unique = true)
    private Balance senderBalance;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "getter_balance_id", unique = true)
    private Balance getterBalance;

    @Column(name = "lock_value")
    private String lockValue;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private PaymentStatus paymentStatus;

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime created;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @Column(name = "clearScheduledAt")
    private ZonedDateTime clearScheduledAt;
}
