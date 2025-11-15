package pe.edu.upc.oncontrol.profile.domain.model.commands.patient;

import java.util.UUID;

public record UpdatePatientProfileCommand(
        UUID patientUuid,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String birthDate,
        String photoUrl
) {
}
