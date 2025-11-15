package pe.edu.upc.oncontrol.appointment.application.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oncontrol.appointment.domain.model.aggregates.Appointment;
import pe.edu.upc.oncontrol.appointment.domain.model.valueobject.AppointmentStatus;
import pe.edu.upc.oncontrol.appointment.infrastructure.persistence.jpa.repositories.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MissedAppointmentsScheduler {
    private final AppointmentRepository appointmentRepository;

    public MissedAppointmentsScheduler(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Scheduled(cron = "0 0 0/6 * * *")
    @Transactional
    public void markAppointmentsAsMissed() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(1);

        List<Appointment> expiredAppointments = appointmentRepository.findByStatusAndScheduledAt_ValueScheduledBefore(
                AppointmentStatus.SCHEDULED, cutoff
        );

        for (Appointment appointment : expiredAppointments) {
            appointment.markAsMissed();
        }
    }


}
