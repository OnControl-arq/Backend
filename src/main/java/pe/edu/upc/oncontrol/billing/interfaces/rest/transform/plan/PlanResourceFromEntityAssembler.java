package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.plan;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Plan;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.plan.PlanResource;

public class PlanResourceFromEntityAssembler {
    public static PlanResource toResourceFromEntity(Plan plan){
        return new PlanResource(
                plan.getId(),
                plan.getName(),
                plan.getPrice().getAmount(),
                plan.getPrice().getCurrencyCode().getCode(),
                plan.getDurationDays(),
                plan.getTrialDays(),
                plan.getMaxPatients(),
                plan.isMessagingEnabled(),
                plan.isSymptomTrackingEnabled(),
                plan.isCustomRemindersEnabled(),
                plan.isCalendarIntegrationEnabled(),
                plan.isBasicReportsEnabled(),
                plan.isAdvancedReportsEnabled(),
                plan.getMaxStorageMb(),
                plan.isActive()
        );
    }
}
