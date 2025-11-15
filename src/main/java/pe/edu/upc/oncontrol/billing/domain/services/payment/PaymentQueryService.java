package pe.edu.upc.oncontrol.billing.domain.services.payment;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentQueryService {
    Optional<Payment> getById(Long paymentId);
    List<Payment> getBySubscriptionId(Long subscriptionId);
}
