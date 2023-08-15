package faang.school.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenExchangeRatesResponseDto {
    private String disclaimer;
    private String license;
    private LocalDateTime timestamp;
    private Currency base;
    private List<Map<Currency, BigDecimal>> rates;
}
