package pe.edu.upc.oncontrol.billing.interfaces.rest.resources.subscriptionkey;

public record SubscriptionKeyPreviewResource(
        Long id,
        String code,
        String status,
        Integer durationDays,
        Long planId
) {
}
