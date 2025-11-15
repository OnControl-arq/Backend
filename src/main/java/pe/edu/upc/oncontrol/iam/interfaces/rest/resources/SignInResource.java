package pe.edu.upc.oncontrol.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignInResource(
        @NotBlank(message = "Username cannot be blank")
        @Size(max = 100, message = "Access identifier cannot exceed 100 characters")
        String identifier,
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 120, message = "Password must be between 6 and 120 characters")
        String password) {
}
