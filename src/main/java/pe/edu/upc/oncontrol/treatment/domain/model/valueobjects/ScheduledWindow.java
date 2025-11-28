package pe.edu.upc.oncontrol.treatment.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class ScheduledWindow {
    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    protected ScheduledWindow() {}

    public ScheduledWindow(LocalDateTime scheduledAt){
        if(scheduledAt == null){
            throw new IllegalStateException("Scheduled time cannot be null");
        }
        this.scheduledAt = scheduledAt;
    }
    public boolean isOnTime(LocalDateTime actual) {
        return !actual.isBefore(scheduledAt.minusMinutes(30))
                && !actual.isAfter(scheduledAt.plusMinutes(30));
    }

    public boolean isRegularized(LocalDateTime actual) {
        return actual.isAfter(scheduledAt.plusMinutes(30))
                && !actual.isAfter(scheduledAt.plusDays(1));
    }

    public boolean isTooLate(LocalDateTime actual) {
        return actual.isAfter(scheduledAt.plusDays(1));
    }

    public LocalDateTime getScheduledAt(){
        return scheduledAt;
    }

    @Override
    public String toString(){
        return "Programmed for " + scheduledAt;
    }
}
