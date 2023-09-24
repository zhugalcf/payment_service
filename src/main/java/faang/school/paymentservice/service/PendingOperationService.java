package faang.school.paymentservice.service;

import faang.school.paymentservice.dto.PendingOperationDto;
import faang.school.paymentservice.entity.operation.OperationStatus;
import faang.school.paymentservice.entity.operation.OperationType;
import faang.school.paymentservice.entity.operation.PendingOperation;
import faang.school.paymentservice.mapper.PendingOperationMapper;
import faang.school.paymentservice.publisher.PendingOperationPublisher;
import faang.school.paymentservice.repostitory.PendingOperationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PendingOperationService {

    private final PendingOperationRepository operationRepository;
    private final PendingOperationMapper operationMapper;
    private final PendingOperationPublisher operationPublisher;

    @Transactional
    public PendingOperationDto initOperation(PendingOperationDto pendingOperationDto, Long userId) {
        PendingOperation operation = operationMapper.toEntity(pendingOperationDto);

        operation.setUserId(userId);
        operation.setOperationStatus(OperationStatus.PENDING);
        operation.setOperationType(OperationType.AUTHORIZATION);
        operation.setScheduledAt(Instant.now().plusMillis(TimeUnit.DAYS.toMillis(10)));
        operation.setCreatedAt(Instant.now());
        operation.setUpdatedAt(Instant.now());
        operation = operationRepository.save(operation);

        PendingOperationDto operationDto = operationMapper.toDto(operation);
        operationPublisher.publish(operationDto);
        log.info("Saved and published pending operation: {}", operationDto);

        return operationDto;
    }
}
