package faang.school.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "open-exchange-rates", url = "${open-exchange-rates.url}")
public interface OpenExchangeRatesClient {

    @GetMapping("/latest.json?app_id=${my-app-id}")
    String getLatestExchangeRates();
}
