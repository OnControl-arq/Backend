package pe.edu.upc.oncontrol.treatment.domain.model.commands.treatment;

import java.time.LocalDate;
import java.util.UUID;

public record CreateTreatmentCommand(
        String title,
        LocalDate startDate,
        LocalDate endDate,
        UUID doctorProfileUuid,
        UUID patientProfileUuid
) {
}
