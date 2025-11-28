package pe.edu.upc.oncontrol.treatment.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record SymptomLogViewResource(
        Long id,
        LocalDateTime loggedAt,
        String symptomType,
        String severity,
        String notes,
        UUID treatmentId,
        Date createdAt
) {
}
