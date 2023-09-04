package faang.school.paymentservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class CurrencyApiResponse {

    @JsonProperty("Timestamp")
    private Instant timeStamp;

    @JsonProperty("Valute")
    private Map<String, CurrencyValue> valute;
}
