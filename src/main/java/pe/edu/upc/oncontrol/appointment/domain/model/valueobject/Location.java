package pe.edu.upc.oncontrol.appointment.domain.model.valueobject;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@Getter
@Embeddable
public class Location implements Serializable {

    private String nameLocation;
    private String mapsUrl;

    protected Location() {}

    public Location(String name, String mapsUrl) {
        if (mapsUrl != null && !mapsUrl.isBlank()) {
            try {
                URI uri = new URI(mapsUrl);
                if (uri.getScheme() == null || uri.getHost() == null) {
                    throw new IllegalArgumentException("mapsUrl must have a valid scheme and host.");
                }
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Invalid mapsUrl.");
            }
        }

        this.nameLocation = name != null ? name.trim() : null;
        this.mapsUrl = mapsUrl != null ? mapsUrl.trim() : null;
    }

    public boolean isPresent(){
        return (nameLocation != null && !nameLocation.isBlank()) || (mapsUrl != null && !mapsUrl.isBlank());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location that)) return false;
        return Objects.equals(nameLocation, that.nameLocation) &&
                Objects.equals(mapsUrl, that.mapsUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameLocation, mapsUrl);
    }
}