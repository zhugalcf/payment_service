package faang.school.paymentservice.currencyRateFetcherService;

import faang.school.paymentservice.exchangeRates.CurrencyData;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CurrencyService {
    private final WebClient webClient;

    public CurrencyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.exchangeratesapi.io").build();
    }

    public CurrencyData fetchCurrencyData() {
        return webClient.get()
                .uri("/latest") // путь к API для получения курсов валют
                .retrieve()
                .bodyToMono(CurrencyData.class)
                .block(); // перестаем получать курсы валют
    }
}
