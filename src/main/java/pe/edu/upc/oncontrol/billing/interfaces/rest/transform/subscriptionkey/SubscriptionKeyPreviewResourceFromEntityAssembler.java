package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.subscriptionkey;

import pe.edu.upc.oncontrol.billing.domain.model.entitites.SubscriptionKey;
import pe.edu.upc.oncontrol.billing.interfaces.rest.resources.subscriptionkey.SubscriptionKeyPreviewResource;

public class SubscriptionKeyPreviewResourceFromEntityAssembler {
    public static SubscriptionKeyPreviewResource toPreviewResourceEntity(SubscriptionKey key){
        return new SubscriptionKeyPreviewResource(
                key.getId(),
                key.getCode().getValue(),
                key.getStatus().name(),
                key.getDurationDays(),
                key.getPlan().getId()
        );
    }
}
