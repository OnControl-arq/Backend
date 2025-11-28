package pe.edu.upc.oncontrol.treatment.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record MarkProcedureExecutionResource(
        @NotNull
        UUID patientProfileUuid,
        @NotNull
        LocalDateTime completionDate
) {
}
