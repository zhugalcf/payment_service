package faang.school.paymentservice.controller;

import faang.school.paymentservice.currencyRateFetcherService.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/fetch-currency-data")
    public ResponseEntity<String> fetchCurrencyData() {
        currencyService.fetchAndSaveCurrencyData();
        return ResponseEntity.ok("Currency data fetched and saved.");
    }
}
