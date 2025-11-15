package pe.edu.upc.oncontrol.profile.interfaces.rest.resources.link;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateLinkResource(
        @NotNull
        UUID doctorUuid,

        @NotNull
        UUID patientUuid
) {
}
