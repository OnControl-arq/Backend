package pe.edu.upc.oncontrol.treatment.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.SymptomSeverity;

import java.time.LocalDateTime;
import java.util.UUID;

public record LogSymptomResource(
        @NotNull
        UUID patientProfileUuid,
        @NotBlank
        @Size(max = 100)
        String symptomType,
        @NotNull
        SymptomSeverity severity,
        @Size(max = 500)
        String notes,
        @NotNull
        @PastOrPresent
        LocalDateTime loggedAt
) {
}
