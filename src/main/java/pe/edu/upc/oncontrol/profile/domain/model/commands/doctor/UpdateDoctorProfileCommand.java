package pe.edu.upc.oncontrol.profile.domain.model.commands.doctor;

import java.util.UUID;

public record UpdateDoctorProfileCommand(
        UUID doctorUuid,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String specialty,
        String CMPCode,
        String photoUrl
) {
}
