package pe.edu.upc.oncontrol.billing.domain.model.entitites;

import pe.edu.upc.oncontrol.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
public class SubscriptionKeyUsage extends AuditableModel {
    @Getter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name ="subscription_key_id", nullable = false)
    private SubscriptionKey subscriptionKey;

    @Getter
    @Column(nullable = false)
    private Long userId;

    @Getter
    @Column(nullable = false)
    private LocalDateTime activatedAt;

    @Getter
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    protected SubscriptionKeyUsage() {}

    public SubscriptionKeyUsage(SubscriptionKey subscriptionKey,Long userId, LocalDateTime now)
    {
        this.subscriptionKey = subscriptionKey;
        this.userId = userId;
        this.activatedAt = now;
        this.expiresAt = now.plusDays(subscriptionKey.getDurationDays());
    }
}
