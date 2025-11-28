package pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure;

import java.util.UUID;

public record CancelProcedureCommand(
        Long procedureId,
        UUID donctorProfileUuid
) {
}
