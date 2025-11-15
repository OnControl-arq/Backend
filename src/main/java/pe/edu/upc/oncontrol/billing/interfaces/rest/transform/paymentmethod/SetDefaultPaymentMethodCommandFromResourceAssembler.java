package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.paymentmethod;

import pe.edu.upc.oncontrol.billing.domain.model.commands.paymentmethod.SetDefaultPaymentMethodCommand;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.paymentmethod.SetDefaultPaymentMethodResource;

public class SetDefaultPaymentMethodCommandFromResourceAssembler {
    public static SetDefaultPaymentMethodCommand toCommand(SetDefaultPaymentMethodResource resource){
        return new SetDefaultPaymentMethodCommand(
                resource.paymentMethodId());
    }
}
