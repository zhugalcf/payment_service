package faang.school.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "open-exchange-rates", url = "https://openexchangerates.org/api")
public interface OpenExchangeRatesClient {

    @GetMapping("/latest.json?app_id=${my-app-id}")
    String getLatestExchangeRates();
}
