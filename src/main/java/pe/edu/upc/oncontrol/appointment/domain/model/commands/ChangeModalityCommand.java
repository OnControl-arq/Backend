package pe.edu.upc.oncontrol.appointment.domain.model.commands;

import java.util.UUID;

public record ChangeModalityCommand(
        Long appointmentId,
        UUID doctorProfileUuid,
        String newLocationName,
        String newLocationMapsUrl,
        String newMeetingUrl
) {
}
