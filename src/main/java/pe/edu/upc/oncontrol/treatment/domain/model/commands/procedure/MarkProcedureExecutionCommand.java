package pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure;

import java.time.LocalDateTime;
import java.util.UUID;

public record MarkProcedureExecutionCommand(
        Long executionId,
        UUID patientProfileUuid,
        LocalDateTime completionDate
) {
}
