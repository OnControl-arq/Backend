package pe.edu.upc.oncontrol.appointment.application.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.oncontrol.appointment.domain.model.aggregates.Appointment;
import pe.edu.upc.oncontrol.appointment.domain.model.valueobject.MeetingUrl;
import pe.edu.upc.oncontrol.appointment.infrastructure.persistence.jpa.repositories.AppointmentRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Component
public class VirtualMeetingScheduler {
    private final AppointmentRepository repository;

    public VirtualMeetingScheduler(AppointmentRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void generateVirtualMeetingUrls() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.minusMinutes(14);
        LocalDateTime to = now.plusMinutes(16);


        List<Appointment> upcoming = repository.findVirtualAppointmentsWithoutMeetingUrl(from, to);

        for (Appointment appointment : upcoming) {
            String formatted = appointment.getScheduledAt().getValueScheduled()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmm"));

            String generatedUrl = "https://meet.jit.si/oncoapp-" + formatted + "-" + UUID.randomUUID();
            System.out.println("✔ Generando enlace para cita ID: " + appointment.getId());
            appointment.changeModality(
                    null,
                    new MeetingUrl(generatedUrl)
            );
        }
    }




}
