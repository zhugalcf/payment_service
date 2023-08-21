package faang.school.paymentservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Map;

@Data
public class CurrencyApiResponse {

    @JsonProperty("Timestamp")
    private Timestamp time_stamp;

    @JsonProperty("Valute")
    private Map<String, CurrencyValue> valute;
}
