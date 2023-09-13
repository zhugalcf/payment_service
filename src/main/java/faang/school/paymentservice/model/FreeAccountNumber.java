package faang.school.paymentservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="free_account_numbers")
public class FreeAccountNumber {

    @EmbeddedId
    private FreeAccountNumberKey id;

    @Column(name="type", nullable = false, insertable = false, updatable = false)
    private AccountType accountType;

    @Column(name = "account_number", nullable = false, insertable = false, updatable = false)
    private BigInteger accountNumber;

    @Embeddable
    @AllArgsConstructor
    public static class FreeAccountNumberKey implements Serializable {
        @Column(name = "type", nullable = false)
        private AccountType accountType;

        @Column(name = "account_number", nullable = false)
        private BigInteger accountNumber;
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof FreeAccountNumberKey)) return false;
            FreeAccountNumberKey that = (FreeAccountNumberKey) o;
            return Objects.equals(accountType, that.accountType) &&
                    Objects.equals(accountNumber, that.accountNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(accountType, accountNumber);
        }
    }
}
