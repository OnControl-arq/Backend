package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.paymentmethod;

import pe.edu.upc.oncontrol.billing.domain.model.commands.paymentmethod.DeactivatePaymentMethodCommand;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.paymentmethod.DeactivatePaymentMethodResource;

public class DeactivatePaymentMethodCommandFromResourceAssembler {
    public static DeactivatePaymentMethodCommand toCommand(DeactivatePaymentMethodResource resource){
        return new DeactivatePaymentMethodCommand(
                resource.paymentMethodId()
        );
    }
}
