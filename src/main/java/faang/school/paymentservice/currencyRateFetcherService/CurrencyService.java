package faang.school.paymentservice.currencyRateFetcherService;

import faang.school.paymentservice.dto.CurrencyData;
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
    private final Map<String, Double> currencyMap = new ConcurrentHashMap<>(); //валюта - курс

    public CurrencyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.exchangeratesapi.io").build();
        fetchAndSaveCurrencyData();
    }

    @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 1000)) // повторить до 2-х раз и с задержкой 1 сек.
    public Map<String, Double> fetchAndSaveCurrencyData() {
        CurrencyData currencyData = webClient.get()
                .uri("/latest?access_key=e63ab22d6e2b5fc267fc5c59237b020a&base=EUR&symbols=USD,RUB,GBP,JPY") // путь к API для получения курсов валют относительно USD
                .retrieve()
                .bodyToMono(CurrencyData.class)
                .block(); // перестаем получать курсы валют

        if (currencyData != null) {
            currencyMap.put(currencyData.getCurrency(), currencyData.getExchangeRate());
            System.out.println("Currency rates: ");
            for (Map.Entry<String, Double> entry : currencyMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
        return currencyMap;
    }

//    public Map<String, Double> getCurrencyDataMap() {
//        return currencyMap;
//    }

    @Recover
    public CurrencyData recover(Exception e) {
        return new CurrencyData(); // вернуть значение по умолчанию
    }
}
