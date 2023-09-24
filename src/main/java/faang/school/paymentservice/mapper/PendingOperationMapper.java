package faang.school.paymentservice.mapper;

import faang.school.paymentservice.dto.PendingOperationDto;
import faang.school.paymentservice.entity.operation.PendingOperation;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PendingOperationMapper {

    PendingOperationDto toDto(PendingOperation pendingOperation);

    PendingOperation toEntity(PendingOperationDto pendingOperationDto);
}
