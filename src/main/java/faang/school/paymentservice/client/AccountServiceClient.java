package faang.school.paymentservice.client;

import faang.school.paymentservice.dto.PaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service", url = "${feign.client.account-service.host}:${feign.client.account-service.port}")
public interface AccountServiceClient {

    @PostMapping("api/v1/payment/create")
    PaymentDto createPayment(@RequestBody PaymentDto paymentDto);
}
