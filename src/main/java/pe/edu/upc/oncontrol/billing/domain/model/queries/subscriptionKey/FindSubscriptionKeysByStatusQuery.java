package pe.edu.upc.oncontrol.billing.domain.model.queries.subscriptionKey;


import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.SubscriptionKeyStatus;

public record FindSubscriptionKeysByStatusQuery(SubscriptionKeyStatus status) {
}
