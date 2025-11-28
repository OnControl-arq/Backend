package pe.edu.upc.oncontrol.treatment.domain.model.valueobjects;

import java.time.LocalDateTime;

public record ProcedureExecutionForecast(
        Long id,
        String procedureName,
        LocalDateTime scheduledAt,
        ExecutionStatus status
) {
}
