package pe.edu.upc.oncontrol.appointment.domain.model.valueobject;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@Getter
@Embeddable
public class MeetingUrl implements Serializable {

    private String url;

    protected MeetingUrl() {
    }

    public MeetingUrl(String url) {
        if (url != null && !url.isBlank()) {
            try {
                URI uri = new URI(url);
                if (uri.getScheme() == null || uri.getHost() == null) {
                    throw new IllegalArgumentException("Meeting URL must have scheme and host.");
                }
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Invalid meeting URL.");
            }
        }
        this.url = url != null ? url.trim() : null;
    }


    public boolean isPresent() {
        return url != null && !url.isBlank();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeetingUrl that)) return false;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}