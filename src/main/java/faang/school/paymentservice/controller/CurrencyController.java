package faang.school.paymentservice.controller;

import faang.school.paymentservice.currencyRateFetcherService.CurrencyServiceForFeign;
import faang.school.paymentservice.dto.CurrencyApiResponse;
import faang.school.paymentservice.dto.PaymentDto;
import faang.school.paymentservice.validate.CurrencyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/v1/currency")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyServiceForFeign currencyServiceForFeign;
    private final CurrencyValidator currencyValidator;

    @PostMapping("/fetch-currency")
    public ResponseEntity<CurrencyApiResponse> fetchCurrencyData() {
        CurrencyApiResponse response = currencyServiceForFeign.fetchAndSaveCurrencyData();
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    public PaymentDto convertCurrency(PaymentDto paymentDto) { // вызов метода в сервисе для обработки платежа
        currencyValidator.validateCurrency(paymentDto);
        return paymentDto;
    }
}
