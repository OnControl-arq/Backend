package pe.edu.upc.oncontrol.billing.interfaces.rest.resources.plan;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateDetailsPlanResource(
        @NotBlank(message = "Name cannot be blank")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name
) {
}
