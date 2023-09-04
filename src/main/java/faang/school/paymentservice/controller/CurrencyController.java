package faang.school.paymentservice.controller;

import faang.school.paymentservice.dto.CurrencyApiResponse;
import faang.school.paymentservice.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/currency")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyServiceForFeign;

    @PostMapping("/fetch")
    public ResponseEntity<CurrencyApiResponse> fetchCurrencyData() {
        CurrencyApiResponse response = currencyServiceForFeign.fetchAndSaveCurrencyData();
        if (response == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
        return ResponseEntity.ok(response);
    }
}