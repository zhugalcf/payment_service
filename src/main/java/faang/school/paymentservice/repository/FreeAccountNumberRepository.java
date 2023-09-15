package faang.school.paymentservice.repository;

import faang.school.paymentservice.model.FreeAccountNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeAccountNumberRepository extends JpaRepository<FreeAccountNumber, FreeAccountNumber.FreeAccountNumberKey> {
}
