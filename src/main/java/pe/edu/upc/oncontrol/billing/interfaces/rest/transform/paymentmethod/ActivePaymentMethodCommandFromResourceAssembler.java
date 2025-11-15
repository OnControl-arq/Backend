package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.paymentmethod;

import pe.edu.upc.oncontrol.billing.domain.model.commands.paymentmethod.ActivePaymentMethodCommand;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.paymentmethod.ActivePaymentMethodCommandResource;

public class ActivePaymentMethodCommandFromResourceAssembler {
    public static ActivePaymentMethodCommand toCommand(ActivePaymentMethodCommandResource resource){
        return new ActivePaymentMethodCommand(
                resource.paymentMethodId()
        );
    }
}
