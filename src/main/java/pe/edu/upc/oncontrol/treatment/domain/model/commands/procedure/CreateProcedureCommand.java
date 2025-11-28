package pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure;

import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.RecurrenceType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateProcedureCommand (
        UUID treatmentExternalId,
        UUID doctorProfileUuid,
        String description,
        RecurrenceType recurrenceType,
        int interval,
        Integer totalOccurrences,
        LocalDate untilDate
){}
