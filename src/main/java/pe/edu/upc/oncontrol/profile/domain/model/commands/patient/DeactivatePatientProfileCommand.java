package pe.edu.upc.oncontrol.profile.domain.model.commands.patient;

import java.util.UUID;

public record DeactivatePatientProfileCommand(UUID patientUuid) {
}
