package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.plan;

import pe.edu.upc.oncontrol.billing.domain.model.commands.plan.CreatePlanCommand;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.plan.PlanResource;

public class CreatePlanCommandFromResourceAssembler {
    public static CreatePlanCommand toCommandFromResource(PlanResource resource){
        return new CreatePlanCommand(
                resource.name(),
                resource.priceAmount(),
                resource.currencyCode(),
                resource.durationDays(),
                resource.trialDays(),
                resource.maxPatients(),
                resource.messagingEnabled(),
                resource.symptomTrackingEnabled(),
                resource.customRemindersEnabled(),
                resource.calendarIntegrationEnabled(),
                resource.basicReportsEnabled(),
                resource.advancedReportsEnabled(),
                resource.maxStorageMb(),
                resource.active()
        );
    }
}
