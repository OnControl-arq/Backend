package pe.edu.upc.oncontrol.profile.domain.model.valueobjects;


import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

@Getter
@Embeddable
public class ContactInfo {
    private String email;
    private String phone;

    protected ContactInfo() {
    }

    public ContactInfo(String email, String phone) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Email is not valid. It cannot be null, empty, or without '@'.");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone cannot be null or empty.");
        }
        this.email = email.trim();
        this.phone = phone.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactInfo that = (ContactInfo) o;
        return Objects.equals(email, that.email) && Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, phone);
    }

    @Override
    public String toString() {
        return "ContactInfo{email='" + email + "', phone='" + phone + "'}";
    }
}