package faang.school.paymentservice.repository;

import faang.school.paymentservice.model.FreeAccountNumber;
import faang.school.paymentservice.model.FreeAccountNumberKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeAccountNumberRepository extends JpaRepository<FreeAccountNumber, FreeAccountNumberKey> {
}
