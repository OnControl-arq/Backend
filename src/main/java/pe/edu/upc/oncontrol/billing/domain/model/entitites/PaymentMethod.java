package pe.edu.upc.oncontrol.billing.domain.model.entitites;

import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.DeactivationReason;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.PaymentMethodType;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.PaymentProvider;
import pe.edu.upc.oncontrol.billing.domain.model.commands.paymentmethod.AddPaymentMethodCommand;
import pe.edu.upc.oncontrol.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
public class PaymentMethod extends AuditableModel {
    @Getter
    @Column(nullable = false)
    private Long adminId;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentProvider provider;

    @Getter
    @Column(nullable = false, unique = true)
    private String providerMethodId;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethodType type;

    @Getter
    private String brand;
    @Getter
    private String last4;
    @Getter
    private Integer expMonth;
    @Getter
    @Min(value = 2024, message = "Expiration year must be 2025 or later")
    private Integer expYear;
    @Setter
    @Getter
    private boolean defaultMethod;
    @Getter
    private boolean active;
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "deactivation_reason", nullable = false)
    private DeactivationReason deactivationReason;
    @Getter
    @Column(name = "deactivated_at")
    private LocalDateTime deactivatedAt;


    public PaymentMethod(){}

    public PaymentMethod(Long userId, AddPaymentMethodCommand command){
        this.adminId = userId;
        this.provider = PaymentProvider.valueOf(command.provider());
        this.providerMethodId = command.providerMethodId();
        this.type = PaymentMethodType.valueOf(command.type());
        this.brand = command.brand();
        this.last4 = command.last4();
        this.expMonth = command.expMonth();
        this.expYear = command.expYear();
        this.defaultMethod = false;
        this.active = true;
        this.deactivationReason = DeactivationReason.NONE;
        this.deactivatedAt = null;
    }

    public PaymentMethod(Long adminId, PaymentProvider provider, String providerMethodId,
                         PaymentMethodType type, String brand, String last4,
                         Integer expMonth, Integer expYear, boolean defaultMethod, boolean active) {
        this.adminId = adminId;
        this.provider = provider;
        this.providerMethodId = providerMethodId;
        this.type = type;
        this.brand = brand;
        this.last4 = last4;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.defaultMethod = defaultMethod;
        this.active = active;
        this.deactivationReason = DeactivationReason.NONE;
        this.deactivatedAt = null;
    }

    public void deactivate(DeactivationReason reason) {
        this.active = false;
        this.defaultMethod = false;
        this.deactivationReason = reason;
        this.deactivatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        this.deactivationReason = DeactivationReason.NONE;
        this.deactivatedAt = null;
    }

}
