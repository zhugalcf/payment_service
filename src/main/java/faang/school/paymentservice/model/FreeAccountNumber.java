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

    @Column(name = "account_type", nullable = false, insertable = false, updatable = false)
    private String accountType;

    @Column(name = "account_number", nullable = false, insertable = false, updatable = false)
    private BigInteger accountNumber;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FreeAccountNumberKey implements Serializable {
        @Column(name = "account_type", nullable = false)
        private String accountType;

        @Column(name = "account_number", nullable = false)
        private BigInteger accountNumber;
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FreeAccountNumberKey that = (FreeAccountNumberKey) o;
            return accountType == that.accountType && Objects.equals(accountNumber, that.accountNumber);
        }

        @Override
        public int hashCode() {
            return Objects.hash(accountType, accountNumber);
        }
    }
}
