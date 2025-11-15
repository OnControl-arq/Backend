package pe.edu.upc.oncontrol.appointment.application.internal.queryservices;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.appointment.domain.model.aggregates.Appointment;
import pe.edu.upc.oncontrol.appointment.domain.services.AppointmentQueryService;
import pe.edu.upc.oncontrol.appointment.infrastructure.persistence.jpa.repositories.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentQueryServiceImpl implements AppointmentQueryService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentQueryServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }
    @Override
    public List<Appointment> getAppointmentsForPatient(UUID patientProfileUuid) {
        return appointmentRepository.findByPatientProfileUuidAndScheduledAt_ValueScheduledAfter(patientProfileUuid, LocalDateTime.now());
    }

    @Override
    public List<Appointment> getAppointmentsForDoctorAndPatient(UUID doctorProfileUuid, UUID patientProfileUuid) {
        return appointmentRepository.findByDoctorProfileUuidAndPatientProfileUuid(doctorProfileUuid, patientProfileUuid);
    }

    @Override
    public List<Appointment> getAppointmentsForDoctor(UUID doctorProfileUuid) {
        return appointmentRepository.findByDoctorProfileUuidAndScheduledAt_ValueScheduledAfter(doctorProfileUuid, LocalDateTime.now());
    }

    @Override
    public Appointment findByIdAndDoctor(Long appointmentId, UUID doctorProfileUuid) {
        return appointmentRepository.findByIdAndDoctorProfileUuid(appointmentId, doctorProfileUuid)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found or access denied"));
    }

    @Override
    public Appointment findByIdAndPatient(Long appointmentId, UUID patientProfileUuid) {
        return appointmentRepository.findByIdAndPatientProfileUuid(appointmentId, patientProfileUuid)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found or access denied"));
    }


}