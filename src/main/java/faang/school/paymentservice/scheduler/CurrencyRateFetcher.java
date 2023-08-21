package faang.school.paymentservice.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CurrencyRateFetcher {

    @Scheduled(cron = "${currency.rate.fetch.cron}")
    public void fetchCurrencyRates() {
    }
}
