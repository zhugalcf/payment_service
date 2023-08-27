package faang.school.paymentservice.dto.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyMap {
    @JsonProperty("CharCode")
    private String charCode;

    @JsonProperty("Value")
    private BigDecimal value;

}
