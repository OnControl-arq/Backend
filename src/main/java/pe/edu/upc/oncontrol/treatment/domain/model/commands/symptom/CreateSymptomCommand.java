package pe.edu.upc.oncontrol.treatment.domain.model.commands.symptom;

import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.SymptomSeverity;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateSymptomCommand(
        UUID treatmentExternalId,
        UUID patientProfileUuid,
        String symptomType,
        SymptomSeverity severity,
        String notes,
        LocalDateTime loggedAt
) {}
