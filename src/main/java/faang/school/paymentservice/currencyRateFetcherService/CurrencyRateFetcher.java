package faang.school.paymentservice.currencyRateFetcherService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CurrencyRateFetcher { //запускается по cron выражению в yaml

    @Scheduled(cron = "${currency.rate.fetch.cron}")
    public void fetchCurrencyRates() {
        System.out.println("Fetching currency rates...");
    }
}
