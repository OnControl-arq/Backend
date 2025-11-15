package pe.edu.upc.oncontrol.appointment.domain.model.commands;

import java.util.UUID;

public record CancelAppointmentByDoctorCommand(
        Long appointmentId,
        UUID doctorProfileUuid
) {
}
