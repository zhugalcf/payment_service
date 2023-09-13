package faang.school.paymentservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;
@AllArgsConstructor
public class FreeAccountNumberKey implements Serializable {
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
