package faang.school.paymentservice.service.payment;

import faang.school.paymentservice.dto.InvoiceDto;
import faang.school.paymentservice.dto.PaymentDto;
import faang.school.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentDto createPayment(InvoiceDto dto) {
        return null;
    }
}
