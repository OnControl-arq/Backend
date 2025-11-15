package pe.edu.upc.oncontrol.billing.interfaces.rest.resources.subscriptionkey;

public record SubscriptionKeyResource(
        Long id,
        String code,
        String status,
        int durationDays,
        Long planId,
        int initialQuantity,
        int quantity) {
}
