package pe.edu.upc.oncontrol.billing.interfaces.rest.resources.subscriptionkey;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UseKeyResource(
        @Min(value = 1, message = "Key ID must be at least 1")
        @NotNull(message = "Subscription key ID cannot be null")
        Long subscriptionKeyId,
        @Min(value = 1, message = "User ID must be at least 1")
        Long userId
) {
}
