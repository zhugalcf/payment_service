package faang.school.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class BalanceDto {
    private Long id;
    private Long accountNumber;
    private BigDecimal authorizationBalance;
    private BigDecimal currentBalance;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Long balanceVersion;
}
