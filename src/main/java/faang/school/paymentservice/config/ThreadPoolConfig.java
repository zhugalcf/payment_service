package faang.school.paymentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

    @Value("${thread-pools.payment.core}")
    private int paymentCoreSize;
    @Value("${thread-pools.payment.max}")
    private int maxPaymentCoreSize;
    @Value("${thread-pools.payment.alive-time}")
    private int aliveTimePayment;
    @Value("${thread-pools.payment.queue}")
    private int paymentQueue;
    @Bean
    public ExecutorService paymentPool(){
        return new ThreadPoolExecutor(paymentCoreSize, maxPaymentCoreSize, aliveTimePayment, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(paymentQueue));
    }
}

