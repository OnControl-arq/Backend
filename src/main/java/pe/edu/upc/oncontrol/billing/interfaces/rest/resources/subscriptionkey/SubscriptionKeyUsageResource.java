package pe.edu.upc.oncontrol.billing.interfaces.rest.resources.subscriptionkey;

import java.time.LocalDateTime;

public record SubscriptionKeyUsageResource(
        Long id,
        Long subscriptionKeyId,
        Long userId,
        LocalDateTime activatedAt,
        LocalDateTime expiresAt
) {
}
