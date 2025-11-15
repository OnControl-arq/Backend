package pe.edu.upc.oncontrol.billing.domain.model.entitites;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Plan;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.SubscriptionKeyCode;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.SubscriptionKeyStatus;
import pe.edu.upc.oncontrol.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
public class SubscriptionKey extends AuditableModel {

    @Getter
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "key_code", unique = true, nullable = false, length = 16))
    private SubscriptionKeyCode code;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SubscriptionKeyStatus status;

    @Getter
    @Column(nullable = false)
    private int durationDays;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="plan_id", nullable = false)
    private Plan plan;

    @Getter
    @Column(nullable = false)
    private int initialQuantity;

    @Getter
    @Column(nullable = false)
    private int quantity;

    protected SubscriptionKey() {}

    public SubscriptionKey(
            String status,
            Plan plan,
            int quantity,
            SubscriptionKeyCode code)
    {
        this.code = code;
        this.status = SubscriptionKeyStatus.valueOf(status.toUpperCase());
        this.durationDays = plan.getDurationDays();
        this.plan = plan;
        this.initialQuantity = quantity;
        this.quantity = quantity;
    }

    public void markAsUsed(){
        this.status = SubscriptionKeyStatus.USED;
    }

    public void expire() {
        this.status = SubscriptionKeyStatus.EXPIRED;
    }

    public void deactivate() {
        if(this.status == SubscriptionKeyStatus.USED || this.status == SubscriptionKeyStatus.EXPIRED) {
            throw new IllegalStateException("Cannot deactivate a used or expired key");
        }
        this.status = SubscriptionKeyStatus.DEACTIVATED;
    }

    public void activate(){
        if(this.status == SubscriptionKeyStatus.DEACTIVATED && this.quantity > 0) {
            this.status = SubscriptionKeyStatus.ACTIVE;
        } else {
            throw new IllegalStateException("Cannot activate a key that is not deactivated or has no remaining uses");
        }
    }

    public void useOnce(){
        if(this.quantity <= 0) throw new IllegalArgumentException("No remaining uses");
        this.quantity--;
        if(this.quantity == 0) {
            markAsUsed();
        }
    }

}
