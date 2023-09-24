package faang.school.paymentservice.repostitory;

import faang.school.paymentservice.entity.operation.PendingOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PendingOperationRepository extends JpaRepository<PendingOperation, UUID> {

}
