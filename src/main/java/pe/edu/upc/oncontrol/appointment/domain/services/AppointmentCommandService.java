package pe.edu.upc.oncontrol.appointment.domain.services;

import pe.edu.upc.oncontrol.appointment.domain.model.commands.*;

public interface AppointmentCommandService {
    void createAppointment(CreateAppointmentCommand command);
    void rescheduleAppointment(RescheduleAppointmentCommand command);
    void changeModality(ChangeModalityCommand command);
    void cancelByDoctor(CancelAppointmentByDoctorCommand command);
    void cancelByPatient(CancelAppointmentByPatientCommand command);
    void markStatus(MarkAppointmentStatusCommand command);
}
