package faang.school.paymentservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(name = "cbr-xml-daily", url = "${external-service.url}")
public interface ExternalServiceClient {

    @GetMapping(value="/daily_json.js", consumes={"application/javascript"})
    String getLatestCurrencyRates();
}
