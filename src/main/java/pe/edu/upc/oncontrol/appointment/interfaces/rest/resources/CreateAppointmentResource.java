package pe.edu.upc.oncontrol.appointment.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateAppointmentResource(
        UUID patientProfileUuid,
        LocalDateTime scheduledAt,
        String locationName,
        String locationMapsUrl,
        String meetingUrl
) {
}
