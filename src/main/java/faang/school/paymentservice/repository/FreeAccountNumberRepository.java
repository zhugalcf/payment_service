package faang.school.paymentservice.repository;

import faang.school.paymentservice.model.FreeAccountNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FreeAccountNumberRepository extends JpaRepository<FreeAccountNumber,
        FreeAccountNumber.FreeAccountNumberKey> {

    @Query(nativeQuery = true, value = """
        DELETE FROM free_account_numbers
        WHERE account_type = :accountType
        AND account_number = (
            SELECT account_number
            FROM free_account_numbers
            WHERE account_type = :accountType
            ORDER BY account_number
            LIMIT 1
        )
        RETURNING account_type, account_number;
        """)
    Optional<FreeAccountNumber> getFreeAccountNumber(String accountType);
}
