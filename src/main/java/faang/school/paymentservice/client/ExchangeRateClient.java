package faang.school.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(name = "cbr-xml-daily", url = "https://www.cbr-xml-daily.ru")
public interface ExchangeRateClient {

    @GetMapping(value = "/daily_json.js", consumes = {"application/javascript"})
    String getLatestExchangeRates();
}
