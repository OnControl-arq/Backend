package pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure;

import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.RecurrenceType;

import java.time.LocalDate;

public record UpdateProcedureCommand(
        Long procedureId,
        String newDescription,
        RecurrenceType newType,
        int newInterval,
        Integer newTotalOccurrences,
        LocalDate newUntilDate
) {
}
