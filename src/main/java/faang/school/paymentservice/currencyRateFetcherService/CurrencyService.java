package faang.school.paymentservice.currencyRateFetcherService;

import faang.school.paymentservice.exchangeRates.CurrencyData;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CurrencyService {
    private final WebClient webClient;
    private final Map<String, Double> currencyMap = new ConcurrentHashMap<>();

    public CurrencyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.exchangeratesapi.io").build();
        fetchAndSaveCurrencyData();
    }

    @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 1000)) // повторить до 2-х раз и с задержкой 1 сек.
    public void fetchAndSaveCurrencyData() {
        CurrencyData currencyData = webClient.get()
                .uri("/latest") // путь к API для получения курсов валют
                .retrieve()
                .bodyToMono(CurrencyData.class)
                .block(); // перестаем получать курсы валют

        if (currencyData != null) {
            currencyMap.put(currencyData.getCurrencyCode(), currencyData.getExchangeRate());
        }
    }

    public Map<String, Double> getCurrencyDataMap() {
        return currencyMap;
    }

    @Recover
    public CurrencyData recover(Exception e) {
        return new CurrencyData(); // вернуть значение по умолчанию
    }
}
