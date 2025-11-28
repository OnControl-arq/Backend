package pe.edu.upc.oncontrol.treatment.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class TreatmentPeriod {

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    protected TreatmentPeriod() {
    }

    public TreatmentPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Dates as cannot be null.");
        }
        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("End date must be after start date.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isActive(LocalDate currentDate) {
        return (currentDate.isEqual(startDate) || currentDate.isAfter(startDate))
                && currentDate.isBefore(endDate);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return startDate + " - " + endDate;
    }
}
