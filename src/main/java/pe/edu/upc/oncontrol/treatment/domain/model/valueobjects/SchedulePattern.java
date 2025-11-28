package pe.edu.upc.oncontrol.treatment.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

@Embeddable
public class SchedulePattern {
    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_type", nullable = false)
    private RecurrenceType type;

    @Column(name = "interval_value", nullable = false)
    private int interval;

    @Column(name = "occurrences")
    private Integer totalOccurrences;

    @Column(name = "until_date")
    private LocalDate untilDate;

    protected SchedulePattern() {
        // JPA
    }

    public SchedulePattern(RecurrenceType type, int interval, Integer totalOccurrences, LocalDate untilDate) {
        if (type == null) throw new IllegalArgumentException("Occurrence type cannot be null.");
        if (interval <= 0) throw new IllegalArgumentException("Interval must be greater than zero.");

        this.type = type;
        this.interval = interval;

        if ((totalOccurrences == null && untilDate == null) || (totalOccurrences != null && untilDate != null)) {
            throw new IllegalArgumentException("You must specify only one of the two: totalOccurrences or untilDate.");        }

        if (totalOccurrences != null) {
            this.totalOccurrences = totalOccurrences;
            this.untilDate = estimateUntilDateFromOccurrences(type, interval, totalOccurrences);
        } else {
            this.untilDate = untilDate;
            this.totalOccurrences = estimateOccurrencesFromUntilDate(type, interval, untilDate);
        }

    }

    public boolean endsByOccurrences() {
        return totalOccurrences != null;
    }

    public boolean endsByDate() {
        return untilDate != null;
    }

    public RecurrenceType getType() {
        return type;
    }

    public int getInterval() {
        return interval;
    }

    public Integer getTotalOccurrences() {
        return totalOccurrences;
    }

    public LocalDate getUntilDate() {
        return untilDate;
    }

    @Override
    public String toString() {
        return type + " every " + interval + (endsByOccurrences() ? " times" : endsByDate() ? " until " + untilDate : "");
    }

    private LocalDate estimateUntilDateFromOccurrences(RecurrenceType type, int interval, int total) {
        return switch (type) {
            case DAILY -> LocalDate.now().plusDays((long) interval * (total - 1));
            case WEEKLY -> LocalDate.now().plusWeeks((long) interval * (total - 1));
            case EVERY_X_HOURS -> LocalDate.now().plusDays((long) Math.ceil((interval * (total - 1)) / 24.0));
            case CUSTOM -> LocalDate.now().plusDays(30);
        };
    }

    private int estimateOccurrencesFromUntilDate(RecurrenceType type, int interval, LocalDate untilDate) {
        long days = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), untilDate);
        return switch (type) {
            case DAILY -> (int) (days / interval) + 1;
            case WEEKLY -> (int) (days / (interval * 7)) + 1;
            case EVERY_X_HOURS -> (int) ((days * 24.0) / interval) + 1;
            case CUSTOM -> 10;
        };
    }


}
