package pe.edu.upc.oncontrol.profile.domain.model.commands.link;

import java.util.UUID;

public record CreateLinkCommand(
        UUID doctorProfileUuid,
        UUID patientProfileUuid
) {
}
