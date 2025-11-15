package pe.edu.upc.oncontrol.appointment.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.edu.upc.oncontrol.appointment.domain.model.valueobject.AppointmentStatus;
import pe.edu.upc.oncontrol.appointment.domain.model.valueobject.Location;
import pe.edu.upc.oncontrol.appointment.domain.model.valueobject.MeetingUrl;
import pe.edu.upc.oncontrol.appointment.domain.model.valueobject.ScheduledAt;
import pe.edu.upc.oncontrol.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
@EntityListeners(AuditingEntityListener.class)
public class Appointment extends AuditableAbstractAggregateRoot<Appointment> {

    @Getter
    @Column(name = "doctor_profile_uuid", nullable = false)
    private UUID doctorProfileUuid;

    @Getter
    @Column(name = "patient_profile_uuid", nullable = false)
    private UUID patientProfileUuid;

    @Getter
    @Embedded
    private ScheduledAt scheduledAt;

    @Getter
    @Embedded
    private Location location;

    @Getter
    @Embedded
    private MeetingUrl meetingUrl;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    protected Appointment() {}

    public Appointment(UUID doctorProfileUuid,
                       UUID patientProfileUuid,
                       ScheduledAt scheduledAt,
                       Location location,
                       MeetingUrl meetingUrl) {
        boolean hasLocation = location != null && location.isPresent();
        boolean hasMeeting = meetingUrl != null && meetingUrl.isPresent();

        if (!hasLocation && !hasMeeting) {
            if (scheduledAt.isBefore(LocalDateTime.now().plusMinutes(15))) {
                throw new IllegalArgumentException("Virtual appointment must have a link if it's within 15 minutes.");
            }
        }


        this.doctorProfileUuid = doctorProfileUuid;
        this.patientProfileUuid = patientProfileUuid;
        this.scheduledAt = scheduledAt;
        this.location = location;
        this.meetingUrl = meetingUrl;
        this.status = AppointmentStatus.SCHEDULED;

    }

    public void changeModality(Location newLocation, MeetingUrl newMeetingUrl) {
        boolean hasLocation = newLocation != null && newLocation.isPresent();
        boolean hasMeeting = newMeetingUrl != null && newMeetingUrl.isPresent();

        if (!hasLocation && !hasMeeting) {
            throw new IllegalArgumentException("Must provide a location or virtual meeting link.");
        }

        this.location = hasLocation ? newLocation : null;
        this.meetingUrl = hasMeeting ? newMeetingUrl : null;
    }

    public void reschedule(ScheduledAt newDateTime, Location newLocation) {
        if (this.status != AppointmentStatus.SCHEDULED) {
            throw new IllegalStateException("Only scheduled appointments can be rescheduled.");
        }
        this.scheduledAt = newDateTime;
        this.location = newLocation;
    }

    public void cancelByDoctor() {
        this.status = AppointmentStatus.CANCELLED_BY_DOCTOR;
    }

    public void cancelByPatient() {
        this.status = AppointmentStatus.CANCELLED_BY_PATIENT;
    }

    public void markAsCompleted() {
        this.status = AppointmentStatus.COMPLETED;
    }

    public void markAsMissed() {
        this.status = AppointmentStatus.MISSED;
    }

    public boolean isFinalized() {
        return this.status == AppointmentStatus.COMPLETED || this.status == AppointmentStatus.MISSED;
    }

}
