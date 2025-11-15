package pe.edu.upc.oncontrol.billing.domain.model.aggregates;

import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.SubscriptionStatus;
import pe.edu.upc.oncontrol.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Subscription extends AuditableAbstractAggregateRoot<Subscription> {
    @Getter
    @Column(name="admin_id", nullable = false)
    private Long adminId;

    @Getter
    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Getter
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Getter
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Getter
    @Column(name="trial_used", nullable = false)
    private boolean trialUsed;

    @Getter
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;



    protected Subscription(){}

    public Subscription(Long adminId, Plan plan, boolean trialUsed) {
        this.adminId = adminId;
        this.plan = plan;
        this.trialUsed = trialUsed;
        this.startDate = LocalDate.now();
        if(trialUsed) {
            this.endDate = this.startDate.plusDays(plan.getDurationDays() + plan.getTrialDays());
        } else{
            this.endDate = this.startDate.plusDays(plan.getDurationDays());
        }
        this.status = SubscriptionStatus.ACTIVE;
        this.cancelledAt = null;
    }

    public Subscription(Long adminId, Plan plan, int durationDays){
        this.adminId = adminId;
        this.plan = plan;
        this.trialUsed = false;
        this.startDate = LocalDate.now();
        this.endDate = this.startDate.plusDays(durationDays);
        this.status = SubscriptionStatus.ACTIVE;
        this.cancelledAt = null;
    }

    public boolean isActive(){
        return status == SubscriptionStatus.ACTIVE && LocalDate.now().isBefore(endDate);
    }

    public void cancel() {
        if (this.status != SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Only active subscriptions can be cancelled");
        }
        this.cancelledAt = LocalDateTime.now();
    }

    public void finalizeCancellation() {
        if (this.cancelledAt == null || this.status != SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Cannot finalize cancellation on a non-cancelled active subscription");
        }
        this.status = SubscriptionStatus.CANCELLED;
    }

    public void forceCancel(){
        this.status = SubscriptionStatus.FORCED_CANCELLED;
        this.endDate = LocalDate.now();
    }

    public void expire(){
        this.status = SubscriptionStatus.EXPIRED;
    }

    public void undoCancellation(){
        if (this.status != SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("Only active subscriptions can be reverted");
        }
        if (this.cancelledAt == null) {
            throw new IllegalStateException("Subscription is not marked for cancellation");
        }
        this.cancelledAt = null;
    }

}
