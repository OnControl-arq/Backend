package pe.edu.upc.oncontrol.profile.interfaces.rest.resources.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PatientProfileUpdateResource(
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
        String photoUrl
) {
}
