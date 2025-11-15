package pe.edu.upc.oncontrol.appointment.domain.services;

import pe.edu.upc.oncontrol.appointment.domain.model.aggregates.Appointment;
import java.util.List;
import java.util.UUID;

public interface AppointmentQueryService {
    List<Appointment> getAppointmentsForPatient(UUID patientProfileUuid);
    List<Appointment> getAppointmentsForDoctor(UUID doctorProfileUuid);
    Appointment findByIdAndDoctor(Long appointmentId, UUID doctorProfileUuid);
    Appointment findByIdAndPatient(Long appointmentId, UUID patientProfileUuid);
    List<Appointment> getAppointmentsForDoctorAndPatient(UUID doctorProfileUuid, UUID patientProfileUuid);
}
