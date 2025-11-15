package pe.edu.upc.oncontrol.appointment.domain.model.commands;

import pe.edu.upc.oncontrol.appointment.domain.model.valueobject.AppointmentStatus;

import java.util.UUID;

public record MarkAppointmentStatusCommand(
        Long appointmentId,
        UUID doctorProfileUuid,
        AppointmentStatus newStatus
) {
}
