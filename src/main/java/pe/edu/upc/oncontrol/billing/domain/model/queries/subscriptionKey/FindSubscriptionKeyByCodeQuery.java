package pe.edu.upc.oncontrol.billing.domain.model.queries.subscriptionKey;

import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.SubscriptionKeyCode;

public record FindSubscriptionKeyByCodeQuery(SubscriptionKeyCode code) {
}
