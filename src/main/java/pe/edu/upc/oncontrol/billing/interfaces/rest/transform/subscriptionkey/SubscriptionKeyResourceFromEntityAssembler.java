package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.subscriptionkey;

import pe.edu.upc.oncontrol.billing.domain.model.entitites.SubscriptionKey;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.subscriptionkey.SubscriptionKeyResource;

public class SubscriptionKeyResourceFromEntityAssembler {
    public static SubscriptionKeyResource toResourceFromEntity(SubscriptionKey subscriptionKey){
        return new SubscriptionKeyResource(
                subscriptionKey.getId(),
                subscriptionKey.getCode().getValue(),
                subscriptionKey.getStatus().name(),
                subscriptionKey.getDurationDays(),
                subscriptionKey.getPlan().getId(),
                subscriptionKey.getInitialQuantity(),
                subscriptionKey.getQuantity()
        );
    }
}
