package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.paymentmethod;

import pe.edu.upc.oncontrol.billing.domain.model.commands.paymentmethod.AddPaymentMethodCommand;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.paymentmethod.PaymentMethodResource;

public class AddPaymentMethodCommandFromResourceAssembler {
    public static AddPaymentMethodCommand toCommandFromResource(PaymentMethodResource resource){
        return new AddPaymentMethodCommand(
                resource.adminId(),
                resource.provider(),
                resource.providerMethodId(),
                resource.type(),
                resource.brand(),
                resource.last4(),
                resource.expMonth(),
                resource.expYear()
        );
    }
}
