package faang.school.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@Builder
public class BalanceDto {
    private Long id;
    private Long accountNumber;
    private BigDecimal authorizationBalance;
    private BigDecimal currentBalance;
    private ZonedDateTime  created;
    private ZonedDateTime updated;
    private Long balanceVersion;
}
