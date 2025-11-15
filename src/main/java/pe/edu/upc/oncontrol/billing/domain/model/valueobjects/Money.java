package pe.edu.upc.oncontrol.billing.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Embeddable
public class Money {
    @Column(name="amount", nullable = false)
    private BigDecimal amount;

    @Embedded
    private CurrencyCode currencyCode;

    protected Money(){}

    public Money(BigDecimal amount, CurrencyCode currencyCode) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO)<0)
            throw new IllegalArgumentException("Amount must be non-negative");
        if(currencyCode == null)
            throw new IllegalArgumentException("Currency code cannot be null");
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

}
