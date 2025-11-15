package pe.edu.upc.oncontrol.appointment.domain.model.valueobject;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


@Getter
@Embeddable
public class ScheduledAt implements Serializable {

    private LocalDateTime valueScheduled;

    protected ScheduledAt() {
    }

    public ScheduledAt(LocalDateTime valueScheduled) {
        if (valueScheduled == null) {
            throw new IllegalArgumentException("Scheduled date cannot be null.");
        }
        if (valueScheduled.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Scheduled date must be in the future.");
        }
        this.valueScheduled = valueScheduled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduledAt that)) return false;
        return Objects.equals(valueScheduled, that.valueScheduled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueScheduled);
    }

    public boolean isBefore(LocalDateTime other) {
        return valueScheduled.isBefore(other);
    }
}
