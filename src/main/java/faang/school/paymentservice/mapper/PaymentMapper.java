package faang.school.paymentservice.mapper;

import faang.school.paymentservice.dto.PaymentDto;
import faang.school.paymentservice.model.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface PaymentMapper {

    Payment toEntity(PaymentDto dto);

    PaymentDto toDto(Payment entity);
}
