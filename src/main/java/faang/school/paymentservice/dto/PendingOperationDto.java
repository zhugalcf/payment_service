package faang.school.paymentservice.dto;

import faang.school.paymentservice.entity.Currency;
import faang.school.paymentservice.entity.operation.OperationStatus;
import faang.school.paymentservice.entity.operation.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingOperationDto {

    private UUID id;

    private OperationType operationType;

    private OperationStatus operationStatus;

    private BigDecimal amount;

    private Long userId;

    private Long senderAccountId;

    private Long receiverAccountId;

    private Currency currency;

    private Instant scheduledAt;

    private Instant createdAt;

    private Instant updatedAt;
}
