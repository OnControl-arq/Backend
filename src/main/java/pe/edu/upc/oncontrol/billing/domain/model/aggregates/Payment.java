package pe.edu.upc.oncontrol.billing.domain.model.aggregates;

import pe.edu.upc.oncontrol.billing.domain.model.commands.payment.RegisterPaymentCommand;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.CurrencyCode;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.Money;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.PaymentProvider;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.PaymentStatus;
import pe.edu.upc.oncontrol.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
public class Payment extends AuditableAbstractAggregateRoot<Payment> {
    @Getter
    @ManyToOne
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @Getter
    @Embedded
    private Money amount;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentProvider provider;

    @Getter
    @Column(name = "provider_payment_id", nullable = false)
    private String providerPaymentId;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Getter
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Getter
    private String errorMessage;

    protected Payment() {}

    public Payment(Subscription subscription, Money amount, PaymentProvider provider,
                   String providerPaymentId){
        this.subscription = subscription;
        this.amount = amount;
        this.provider = provider;
        this.providerPaymentId = providerPaymentId;
        this.status = PaymentStatus.PENDING;
    }

    public static Payment create(RegisterPaymentCommand command, Subscription subscription){
        return new Payment(subscription,
                new Money(command.amount(), new CurrencyCode(command.currencyCode())),
                PaymentProvider.valueOf(command.provider()),
                command.providerPaymentId());
    }

    public void markAsPaid(){
        this.status = PaymentStatus.PAID;
        this.paidAt = LocalDateTime.now();
    }

    public void markAsFailed(String errorMessage){
        this.status = PaymentStatus.FAILED;
        this.errorMessage = errorMessage;
    }

    public void cancel(){
        this.status = PaymentStatus.CANCELLED;
    }
}
