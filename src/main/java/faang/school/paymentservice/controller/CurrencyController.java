package faang.school.paymentservice.controller;

import faang.school.paymentservice.currencyRateFetcherService.CurrencyService;
import faang.school.paymentservice.dto.PaymentDto;
import faang.school.paymentservice.validate.CurrencyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;
    private final CurrencyValidator currencyValidator;

    @GetMapping("/fetch-currency-data")
    public ResponseEntity<String> fetchCurrencyData() {
        currencyService.fetchAndSaveCurrencyData();
        return ResponseEntity.ok("Currency data fetched and saved.");
    }

    public PaymentDto convertCurrency(PaymentDto paymentDto) {
        currencyValidator.validateCurrency(paymentDto);
        return null;
    }
}
