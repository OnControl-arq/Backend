package pe.edu.upc.oncontrol.profile.interfaces.rest.resources.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PatientProfileCreateResource(
        @NotNull
        Long userId,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @Email
        @NotBlank
        String email,
        @NotBlank
        String phoneNumber,
        @NotBlank
        String birthDate,
        @NotBlank
        String gender,
        @NotBlank
        String photoUrl
) {
}
