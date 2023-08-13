package faang.school.paymentservice.currencyRateFetcherService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(name = "cbr-xml-daily", url = "https://www.cbr-xml-daily.ru")
public interface ExternalServiceClient {

    @GetMapping(value="/daily_json.js", consumes={"application/javascript"})
    String getLatestCurrencyRates();
}
