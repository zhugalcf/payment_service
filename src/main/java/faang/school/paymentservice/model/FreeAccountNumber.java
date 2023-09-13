package faang.school.paymentservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="free_account_numbers")
@IdClass(FreeAccountNumberKey.class)
public class FreeAccountNumber {
    @Id
    @Column(name="type", nullable = false)
    private AccountType accountType;
    @Id
    @Column(name="account_number", nullable = false)
    private BigInteger accountNumber;
}
