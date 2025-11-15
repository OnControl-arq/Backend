package pe.edu.upc.oncontrol.profile.domain.model.commands.doctor;

import java.util.UUID;

public record DeactivateDoctorProfileCommand(UUID doctorUuid) {
}
