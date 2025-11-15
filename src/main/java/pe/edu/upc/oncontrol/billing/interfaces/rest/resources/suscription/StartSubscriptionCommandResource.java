package pe.edu.upc.oncontrol.billing.interfaces.rest.resources.suscription;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StartSubscriptionCommandResource(
        @NotNull(message = "Admin ID cannot be null")
        @Min(value = 1, message = "Admin ID invalid")
        Long adminId,
        @NotNull(message = "Plan ID cannot be null")
        @Min(value = 1, message = "Plan ID invalid")
        Long planId
) {
}
