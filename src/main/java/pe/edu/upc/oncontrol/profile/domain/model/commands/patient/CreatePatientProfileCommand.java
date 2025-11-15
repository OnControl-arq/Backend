package pe.edu.upc.oncontrol.profile.domain.model.commands.patient;

import java.time.LocalDate;

public record CreatePatientProfileCommand(
        Long userId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String birthDate,
        String gender,
        String photoUrl
) {
}
