package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.subscription;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Subscription;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.suscription.SubscriptionResource;

public class SubscriptionResourceEntityAssembler {
    public static SubscriptionResource toResourceFromEntity(Subscription subscription){
        return new SubscriptionResource(
                subscription.getId(),
                subscription.getAdminId(),
                subscription.getPlan().getId(),
                subscription.getStatus().name(),
                subscription.getStartDate(),
                subscription.getEndDate(),
                subscription.isTrialUsed(),
                subscription.getCancelledAt()
        );
    }
}
