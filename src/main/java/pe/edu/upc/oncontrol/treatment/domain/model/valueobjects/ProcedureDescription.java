package pe.edu.upc.oncontrol.treatment.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProcedureDescription {
    @Column(name = "description", nullable = false, length = 500)
    private String value;

    protected ProcedureDescription() {
        // JPA
    }

    public ProcedureDescription(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        if (value.length() > 500) {
            throw new IllegalArgumentException("Description cannot exceed 500 characters.");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

}
