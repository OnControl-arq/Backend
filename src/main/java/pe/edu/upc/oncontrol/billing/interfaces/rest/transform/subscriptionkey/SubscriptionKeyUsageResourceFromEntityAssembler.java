package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.subscriptionkey;

import pe.edu.upc.oncontrol.billing.domain.model.entitites.SubscriptionKeyUsage;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.subscriptionkey.SubscriptionKeyUsageResource;

public class SubscriptionKeyUsageResourceFromEntityAssembler {
    public static SubscriptionKeyUsageResource toResourceFromEntity(SubscriptionKeyUsage usage){
        return new SubscriptionKeyUsageResource(
                usage.getId(),
                usage.getSubscriptionKey().getId(),
                usage.getUserId(),
                usage.getActivatedAt(),
                usage.getExpiresAt()
        );
    }
}
