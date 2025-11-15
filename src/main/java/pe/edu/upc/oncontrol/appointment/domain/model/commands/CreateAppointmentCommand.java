package pe.edu.upc.oncontrol.appointment.domain.model.commands;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateAppointmentCommand(
        UUID doctorProfileUuid,
        UUID patientProfileUuid,
        LocalDateTime scheduledAt,
        String locationName,
        String locationMapsUrl,
        String meetingUrl
) {
}
