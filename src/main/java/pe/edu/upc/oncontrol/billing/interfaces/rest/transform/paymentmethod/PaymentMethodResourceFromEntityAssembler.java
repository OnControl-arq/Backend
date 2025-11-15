package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.paymentmethod;

import pe.edu.upc.oncontrol.billing.domain.model.entitites.PaymentMethod;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.paymentmethod.PaymentMethodResource;

public class PaymentMethodResourceFromEntityAssembler {
    public static PaymentMethodResource toResourceFromEntity(PaymentMethod paymentMethod){
        return new PaymentMethodResource(
                paymentMethod.getId(),
                paymentMethod.getAdminId(),
                paymentMethod.getProvider().name(),
                paymentMethod.getProviderMethodId(),
                paymentMethod.getType().name(),
                paymentMethod.getBrand(),
                paymentMethod.getLast4(),
                paymentMethod.getExpMonth(),
                paymentMethod.getExpYear(),
                paymentMethod.isActive()
        );
    }
}
