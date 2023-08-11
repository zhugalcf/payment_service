package faang.school.paymentservice.controller;

import faang.school.paymentservice.currencyRateFetcherService.CurrencyService;
import faang.school.paymentservice.dto.PaymentDto;
import faang.school.paymentservice.validate.CurrencyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/currency")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;
    private final CurrencyValidator currencyValidator;

    @GetMapping("/new")
    public ResponseEntity<Map<String, Double>> fetchCurrencyRates() {
        Map<String, Double> currencyRates = currencyService.fetchAndSaveCurrencyData();
        return ResponseEntity.ok(currencyRates);
    }

    public PaymentDto convertCurrency(PaymentDto paymentDto) { // вызов метода в сервисе для обработки платежа
        currencyValidator.validateCurrency(paymentDto);
        return paymentDto;
    }
}
