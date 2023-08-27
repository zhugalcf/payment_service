package faang.school.paymentservice.dto.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyApiResponse {
    @JsonProperty("Date")
    private String date;

    @JsonProperty("PreviousDate")
    private String previousDate;

    @JsonProperty("PreviousURL")
    private String previousUrl;

    @JsonProperty("Timestamp")
    private String timestamp;

    @JsonProperty("Valute")
    private Map<String, CurrencyMap> valute;
}
