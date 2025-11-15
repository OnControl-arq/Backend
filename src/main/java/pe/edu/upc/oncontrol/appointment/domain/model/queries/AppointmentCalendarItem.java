package pe.edu.upc.oncontrol.appointment.domain.model.queries;

import java.time.LocalDateTime;

public record AppointmentCalendarItem(
        Long id,
        String title,
        LocalDateTime scheduledAt,
        String status,
        String type
) {
}
