package pe.edu.upc.oncontrol.billing.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class CurrencyCode{
    @Column(name ="currency_code", length = 3, nullable = false)
    private String code;
    public CurrencyCode(String code) {
        if (code == null || code.isBlank())
            throw new IllegalArgumentException("Currency code cannot be null or blank");
        if (!code.matches("^[A-Z]{3}$"))
            throw new IllegalArgumentException("Currency code must be a 3-letter ISO 4217 code");
        this.code = code;
    }

    public CurrencyCode() {

    }

}
