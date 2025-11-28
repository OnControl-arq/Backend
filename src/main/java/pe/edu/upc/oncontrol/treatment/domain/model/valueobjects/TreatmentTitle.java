package pe.edu.upc.oncontrol.treatment.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class TreatmentTitle {

    @Column(name = "title", nullable = false, length = 100)
    private String value;

    protected TreatmentTitle() {
    }

    public TreatmentTitle(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be blank or null.");
        }
        if (value.length() < 3 || value.length() > 100) {
            throw new IllegalArgumentException("Title must be between 3 and 100 characters long.");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreatmentTitle that)) return false;
        return value.equalsIgnoreCase(that.value);
    }

    @Override
    public int hashCode() {
        return value.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
