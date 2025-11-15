package pe.edu.upc.oncontrol.profile.domain.model.commands.doctor;

public record CreateDoctorProfileCommand(
        Long userId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String documentType,
        String documentNumber,
        String specialty,
        String CMPCode,
        String photoUrl
) {
}
