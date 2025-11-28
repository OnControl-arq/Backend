package pe.edu.upc.oncontrol.treatment.interfaces.rest.resources;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record CreateTreatmentResource(
        @NotBlank
        @Size(max = 100)
        String title,
        @NotNull
        LocalDate startDate,
        @Future
        LocalDate endDate,
        @NotNull
        UUID doctorProfileUuid,
        @NotNull
        UUID patientProfileUuid
) {
}
