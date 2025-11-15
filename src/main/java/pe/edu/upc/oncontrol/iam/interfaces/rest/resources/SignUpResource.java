package pe.edu.upc.oncontrol.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import pe.edu.upc.oncontrol.iam.domain.model.valueobjects.Roles;

public record SignUpResource(
        @NotBlank(message = "Username cannot be blank")
        @Size(min=7 , max=50, message = "Username cannot exceed 50 characters")
        String username,
        @Email(message = "Format of email is invalid")
        @Size(max=100, message = "Email cannot exceed 100 characters")
        String email,
        @NotBlank(message = "Password cannot be back")
        @Size(min = 8, max = 120, message = "Password must be between 8 and 120 characters")
        String password,
        Roles role
        ) {}
