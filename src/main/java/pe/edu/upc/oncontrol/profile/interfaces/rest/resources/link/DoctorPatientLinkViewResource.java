package pe.edu.upc.oncontrol.profile.interfaces.rest.resources.link;

import pe.edu.upc.oncontrol.profile.domain.model.valueobjects.LinkStatus;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record DoctorPatientLinkViewResource(
        UUID externalId,
        UUID doctorUuid,
        UUID patientUuid,
        String doctorFullName,
        String patientFullName,
        LinkStatus status,
        Date createdAt,
        LocalDateTime disabledAt
) {
}
