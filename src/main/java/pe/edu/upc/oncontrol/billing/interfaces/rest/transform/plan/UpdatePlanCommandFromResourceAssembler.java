package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.plan;

import pe.edu.upc.oncontrol.billing.domain.model.commands.plan.UpdatePlanCommand;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.plan.UpdateDetailsPlanResource;

public class UpdatePlanCommandFromResourceAssembler {
    public static UpdatePlanCommand toCommandFromResource(Long id, UpdateDetailsPlanResource resource){
        return new UpdatePlanCommand(
                id,
                resource.name()
        );
    }
}
