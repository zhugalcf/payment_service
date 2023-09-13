package faang.school.paymentservice.repository;

import faang.school.paymentservice.model.AccountNumbersSequence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountNumbersSequenceRepository extends JpaRepository<AccountNumbersSequence, Long> {
}
