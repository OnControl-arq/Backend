package pe.edu.upc.oncontrol.treatment.domain.model.commands.treatment;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateTreatmentCommand(
        UUID treatmentExternalId,
        String newTitle,
        LocalDate newStartDate,
        LocalDate newEndDate
) {
}
