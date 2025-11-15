package pe.edu.upc.oncontrol.profile.domain.model.valueobjects;


import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

@Getter
@Embeddable
public class DocumentIdent {
    private String type;
    private String number;

    protected DocumentIdent() {
    }

    public DocumentIdent(String type, String number) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("The document type cannot be null or empty.");
        }
        if (!type.equalsIgnoreCase("DNI") && !type.equalsIgnoreCase("Alien registration card")) {
            throw new IllegalArgumentException("The document type must be either 'DNI' or 'Alien registration card'.");
        }
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("The document number cannot be null or empty.");
        }
        this.type = type.trim();
        this.number = number.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentIdent that = (DocumentIdent) o;
        return Objects.equals(type, that.type) && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, number);
    }

    @Override
    public String toString() {
        return "DocumentIdent{type='" + type + "', number='" + number + "'}";
    }
}