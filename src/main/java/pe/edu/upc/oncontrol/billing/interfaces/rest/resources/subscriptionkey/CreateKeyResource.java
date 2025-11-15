package pe.edu.upc.oncontrol.billing.interfaces.rest.resources.subscriptionkey;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateKeyResource(
        @NotBlank(message = "Code cannot be blank")
        String status,
        @NotNull(message = "Duration days cannot be null")
        @Min(value = 1, message = "Duration days must be at least 1")
        Long planId,
        @NotNull(message = "Initial quantity cannot be null")
        @Min(value = 1, message = "Initial quantity must be at least 1")
        @Max(value = 5, message = "Initial quantity must not exceed 5")
        Integer quantity
) {
}
