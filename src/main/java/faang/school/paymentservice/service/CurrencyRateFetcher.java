package faang.school.paymentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrencyRateFetcher {

    @Scheduled(cron = "${currency.rate.fetch.cron}")
    public void getCurrencyRate() {
        System.out.println("Getting currency rate");
    }
}
