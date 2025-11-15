package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.subscription;

import pe.edu.upc.oncontrol.billing.domain.model.commands.subscription.StartSubscriptionCommand;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.suscription.StartSubscriptionCommandResource;

public class StartSubscriptionCommandFromResourceAssembler {
    public static StartSubscriptionCommand toCommandFromResource(StartSubscriptionCommandResource resource){
        return new StartSubscriptionCommand(
                resource.adminId(),
                resource.planId()
        );
    }
}
