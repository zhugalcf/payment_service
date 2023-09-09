package faang.school.paymentservice.mapper;

import faang.school.paymentservice.dto.BalanceDto;
import faang.school.paymentservice.model.Balance;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BalanceMapper {
    BalanceDto toDto(Balance balance);
    Balance toEntity(BalanceDto balanceDto);
}
