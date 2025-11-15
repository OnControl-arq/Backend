package pe.edu.upc.oncontrol.appointment.interfaces.rest.resources;

import java.time.LocalDateTime;

public record AppointmentCalendarItem(
        Long id,
        String title,
        LocalDateTime scheduledAt,
        String status,
        String type
) {
}