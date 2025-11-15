package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.subscriptionkey;

import pe.edu.upc.oncontrol.billing.domain.model.commands.subscriptionkey.UseSubscriptionKeyCommand;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.subscriptionkey.UseKeyResource;

public class UseKeyCommandFromResourceAssembler {
    public static UseSubscriptionKeyCommand toCommandFromResource(UseKeyResource resource){
        return new UseSubscriptionKeyCommand(resource.subscriptionKeyId(), resource.userId());
    }
}
