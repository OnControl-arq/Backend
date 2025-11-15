package pe.edu.upc.oncontrol.profile.interfaces.rest.resources.doctor;

import java.util.UUID;

public record DoctorProfileViewResource(
        UUID uuid,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String documentType,
        String documentNumber,
        String specialty,
        String CMPCode,
        String urlPhoto,
        boolean active
) {
}
