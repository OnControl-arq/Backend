package pe.edu.upc.oncontrol.billing.application.internal.queryservices;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Payment;
import pe.edu.upc.oncontrol.billing.domain.services.payment.PaymentQueryService;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentQueryServiceImpl implements PaymentQueryService {

    private final PaymentRepository paymentRepository;

    public PaymentQueryServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Optional<Payment> getById(Long paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getBySubscriptionId(Long subscriptionId) {
        return paymentRepository.findALLBySubscriptionIdOrderByPaidAtDesc(subscriptionId);
    }
}
