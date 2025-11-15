package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.payment;

import pe.edu.upc.oncontrol.billing.domain.model.commands.payment.RegisterPaymentCommand;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.payment.PaymentResource;

public class RegisterPaymentCommandFromResourceAssembler {
    public static RegisterPaymentCommand toCommandFromResource(PaymentResource resource){
        return new RegisterPaymentCommand(
                resource.subscriptionId(),
                resource.amount(),
                resource.currencyCode(),
                resource.provider(),
                resource.providerPaymentId()
        );
    }
}
