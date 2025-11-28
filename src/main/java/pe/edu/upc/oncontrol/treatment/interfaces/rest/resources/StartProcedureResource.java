package pe.edu.upc.oncontrol.treatment.interfaces.rest.resources;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record StartProcedureResource(
        @NotNull
        UUID patientProfileUuid,
        @NotNull
        @FutureOrPresent
        LocalDateTime startDateTime
) {
}
