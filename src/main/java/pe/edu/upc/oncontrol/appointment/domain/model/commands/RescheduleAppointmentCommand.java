package pe.edu.upc.oncontrol.appointment.domain.model.commands;

import java.time.LocalDateTime;
import java.util.UUID;

public record RescheduleAppointmentCommand(
        Long appointmentId,
        UUID doctorProfileUuid,
        LocalDateTime newScheduledAt,
        String newLocationName,
        String newLocationMapsUrl
) {
}
