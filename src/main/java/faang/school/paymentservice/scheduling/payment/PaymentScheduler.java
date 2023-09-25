package faang.school.paymentservice.scheduling.payment;

import faang.school.paymentservice.model.Payment;
import faang.school.paymentservice.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentScheduler {

    private final PaymentService service;
    private final ThreadPoolTaskScheduler paymentTaskScheduler;

    @Async
    @Scheduled(cron = "${scheduling.gather-payment}")
    public void getScheduledPayment() {
        List<Payment> payments = service.getScheduledPayment();

        for (Payment payment : payments) {
            paymentTaskScheduler.schedule(() ->
                            service.clear(payment),
                    payment.getScheduledAt().toInstant(ZoneOffset.UTC));
        }
    }
}
