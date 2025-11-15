package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.payment;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Payment;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.payment.PaymentResource;

public class PaymentResourceFromEntityAssembler {
    public static PaymentResource toResourceFromEntity(Payment payment){
        return new PaymentResource(
                payment.getSubscription().getId(),
                payment.getAmount().getAmount(),
                payment.getAmount().getCurrencyCode().getCode(),
                payment.getProvider().name(),
                payment.getProviderPaymentId()
        );
    }
}
