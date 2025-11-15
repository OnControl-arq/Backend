package pe.edu.upc.oncontrol.profile.interfaces.rest.resources.patient;

import java.util.UUID;

public record PatientProfileViewResource(
        UUID uuid,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String birthDate,
        String gender,
        String photoUrl,
        boolean active
) {
}
