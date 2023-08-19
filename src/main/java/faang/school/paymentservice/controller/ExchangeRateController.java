package faang.school.paymentservice.controller;

import faang.school.paymentservice.dto.exchange.CurrencyApiResponse;
import faang.school.paymentservice.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/currency")
@RequiredArgsConstructor
public class ExchangeRateController {
    private final CurrencyService currencyService;

    @GetMapping("/exchange")
    public ResponseEntity<CurrencyApiResponse> getCurrencyRate() {
        CurrencyApiResponse currencyApiResponse = currencyService.getCurrencyRate();
        if (currencyApiResponse != null) {
            return ResponseEntity.ok(currencyApiResponse);
        }else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }


}
