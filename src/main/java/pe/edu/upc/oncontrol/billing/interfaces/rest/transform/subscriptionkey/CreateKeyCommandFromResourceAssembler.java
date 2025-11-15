package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.subscriptionkey;

import pe.edu.upc.oncontrol.billing.domain.model.commands.subscriptionkey.CreateKeyCommand;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.subscriptionkey.CreateKeyResource;

public class CreateKeyCommandFromResourceAssembler {
    public static CreateKeyCommand toCommandFromResource(CreateKeyResource resource){
        return new CreateKeyCommand(
                resource.status(),
                resource.planId(),
                resource.quantity()
        );
    }
}
