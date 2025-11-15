package pe.edu.upc.oncontrol.appointment.interfaces.rest.resources;

import java.time.LocalDateTime;

public record AppointmentDetail(
        Long id,
        LocalDateTime scheduledAt,
        String status,
        String locationName,
        String locationMapsUrl,
        String meetingUrl,
        String patientProfileUuid,
        String doctorProfileUuid
) {
}
