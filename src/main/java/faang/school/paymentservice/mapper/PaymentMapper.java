package faang.school.paymentservice.mapper;

import faang.school.paymentservice.dto.PaymentDto;
import faang.school.paymentservice.dto.payment.PaymentEvent;
import faang.school.paymentservice.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface PaymentMapper {

    Payment toEntity(PaymentDto dto);

    PaymentDto toDto(Payment entity);

    @Mapping(target = "type", source = "status")
    PaymentEvent toEvent(Payment payment);
}
