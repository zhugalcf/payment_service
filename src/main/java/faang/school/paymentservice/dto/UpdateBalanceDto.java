package faang.school.paymentservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class UpdateBalanceDto {
    @NotNull
    private Long balanceId;
    @NotNull
    private BigDecimal deposit;
}
