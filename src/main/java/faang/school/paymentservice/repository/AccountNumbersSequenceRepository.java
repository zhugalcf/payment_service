package faang.school.paymentservice.repository;

import faang.school.paymentservice.model.AccountNumbersSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface AccountNumbersSequenceRepository extends JpaRepository<AccountNumbersSequence, Long> {
    Optional<AccountNumbersSequence> findByAccountType(String accountType);

    @Query(nativeQuery = true, value = """
            UPDATE account_number_sequence 
            SET current_number = current_number + 1
            WHERE account_type = :accountType
            RETURNING current_number""")
    Optional<BigInteger>  incrementCounter(@Param("accountType") String accountType);
}
