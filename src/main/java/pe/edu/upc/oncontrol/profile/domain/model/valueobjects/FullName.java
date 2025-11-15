package pe.edu.upc.oncontrol.profile.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

@Getter
@Embeddable
public class FullName {
    private String firstName;
    private String lastName;

    protected FullName() {
    }

    public FullName(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("The first name cannot be null or empty.");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("The last name cannot be null or empty.");
        }
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullName fullName = (FullName) o;
        return Objects.equals(firstName, fullName.firstName) && Objects.equals(lastName, fullName.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return getFullName();
    }
}