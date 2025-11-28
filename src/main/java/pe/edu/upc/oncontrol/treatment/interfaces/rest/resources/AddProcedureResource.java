package pe.edu.upc.oncontrol.treatment.interfaces.rest.resources;

import jakarta.validation.constraints.*;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.RecurrenceType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record AddProcedureResource(
        @NotNull
        UUID doctorProfileUuid,
        @NotBlank
        @Size(max = 500)
        String description,
        @NotNull
        RecurrenceType recurrenceType,
        @Min(1)
        int interval,
        @Positive
        Integer totalOccurrences,
        @Future
        LocalDate untilDate
) {
        @AssertTrue(message = "You must specify either totalOccurrences or untilDate, but not both.")
        public boolean onlyOneLimitDefined() {
                return (totalOccurrences == null) != (untilDate == null);
        }
}
