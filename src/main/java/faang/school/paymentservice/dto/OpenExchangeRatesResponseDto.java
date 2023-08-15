package faang.school.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenExchangeRatesResponseDto {
    private String disclaimer;
    private String license;
    private Timestamp timestamp;
    private Currency base;
    private Map<String, BigDecimal> rates;
}
