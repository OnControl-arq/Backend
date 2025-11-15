package pe.edu.upc.oncontrol.appointment.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oncontrol.appointment.domain.model.aggregates.Appointment;
import pe.edu.upc.oncontrol.appointment.domain.model.commands.*;
import pe.edu.upc.oncontrol.appointment.domain.model.valueobject.AppointmentStatus;
import pe.edu.upc.oncontrol.appointment.domain.model.valueobject.Location;
import pe.edu.upc.oncontrol.appointment.domain.model.valueobject.MeetingUrl;
import pe.edu.upc.oncontrol.appointment.domain.model.valueobject.ScheduledAt;
import pe.edu.upc.oncontrol.appointment.domain.services.AppointmentCommandService;
import pe.edu.upc.oncontrol.appointment.infrastructure.persistence.jpa.repositories.AppointmentRepository;
import pe.edu.upc.oncontrol.profile.application.acl.ProfileAccessAcl;

@Service
public class AppointmentCommandServiceImpl implements AppointmentCommandService {

    private final AppointmentRepository appointmentRepository;
    private final ProfileAccessAcl profileAccessAcl;

    public AppointmentCommandServiceImpl(AppointmentRepository appointmentRepository, ProfileAccessAcl profileAccessAcl) {
        this.appointmentRepository = appointmentRepository;
        this.profileAccessAcl = profileAccessAcl;
    }


    @Override
    @Transactional
    public void createAppointment(CreateAppointmentCommand command) {
        if (!profileAccessAcl.isLinkActive(command.doctorProfileUuid(), command.patientProfileUuid())) {
            throw new IllegalStateException("Doctor and patient are not linked or link is not active.");
        }

        Location location = (command.locationName() != null || command.locationMapsUrl() != null)
                ? new Location(command.locationName(), command.locationMapsUrl())
                : null;

        MeetingUrl meetingUrl = (command.meetingUrl() != null && !command.meetingUrl().isBlank())
                ? new MeetingUrl(command.meetingUrl())
                : null;

        Appointment appointment = new Appointment(
                command.doctorProfileUuid(),
                command.patientProfileUuid(),
                new ScheduledAt(command.scheduledAt()),
                location,
                meetingUrl
        );

        appointmentRepository.save(appointment);
    }


    @Override
    @Transactional
    public void rescheduleAppointment(RescheduleAppointmentCommand command) {
        Appointment appointment = appointmentRepository.findByIdAndDoctorProfileUuid(command.appointmentId(), command.doctorProfileUuid())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found or access denied."));

        appointment.reschedule(
                new ScheduledAt(command.newScheduledAt()),
                new Location(command.newLocationName(), command.newLocationMapsUrl())
        );
    }

    @Override
    @Transactional
    public void changeModality(ChangeModalityCommand command) {
        Appointment appointment = appointmentRepository.findByIdAndDoctorProfileUuid(command.appointmentId(), command.doctorProfileUuid())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found or access denied."));

        System.out.println("✔ Generando enlace para cita ID: " + appointment.getId());
        appointment.changeModality(
                new Location(command.newLocationName(), command.newLocationMapsUrl()),
                new MeetingUrl(command.newMeetingUrl())
        );
    }

    @Override
    @Transactional
    public void cancelByDoctor(CancelAppointmentByDoctorCommand command) {
        Appointment appointment = appointmentRepository.findByIdAndDoctorProfileUuid(command.appointmentId(), command.doctorProfileUuid())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found or access denied."));

        appointment.cancelByDoctor();
    }

    @Override
    @Transactional
    public void cancelByPatient(CancelAppointmentByPatientCommand command) {
        Appointment appointment = appointmentRepository.findByIdAndPatientProfileUuid(command.appointmentId(), command.patientProfileUuid())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found or access denied."));

        appointment.cancelByPatient();
    }

    @Override
    @Transactional
    public void markStatus(MarkAppointmentStatusCommand command) {

        if (command.newStatus() == AppointmentStatus.CANCELLED_BY_DOCTOR ||
                command.newStatus() == AppointmentStatus.CANCELLED_BY_PATIENT) {
            throw new IllegalArgumentException("Use cancelByDoctor or cancelByPatient for cancelling appointments.");
        }

        if (command.newStatus() != AppointmentStatus.COMPLETED &&
                command.newStatus() != AppointmentStatus.MISSED) {
            throw new IllegalArgumentException("Only COMPLETED or MISSED can be assigned via this method.");
        }

        Appointment appointment = appointmentRepository.findByIdAndDoctorProfileUuid(command.appointmentId(), command.doctorProfileUuid())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found or access denied."));

        if (command.newStatus() == AppointmentStatus.COMPLETED) {
            appointment.markAsCompleted();
        } else {
            appointment.markAsMissed();
        }
    }
}
