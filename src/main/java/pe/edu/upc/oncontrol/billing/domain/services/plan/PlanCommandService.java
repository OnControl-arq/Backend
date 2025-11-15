package pe.edu.upc.oncontrol.billing.domain.services.plan;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Plan;
import pe.edu.upc.oncontrol.billing.domain.model.commands.plan.ActivatePlanCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.plan.CreatePlanCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.plan.DeactivatePlanCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.plan.UpdatePlanCommand;

public interface PlanCommandService {
    Plan update(UpdatePlanCommand command);
    Plan create(CreatePlanCommand command);
    void deactivate(DeactivatePlanCommand command);
    Plan activate(ActivatePlanCommand command);
}
