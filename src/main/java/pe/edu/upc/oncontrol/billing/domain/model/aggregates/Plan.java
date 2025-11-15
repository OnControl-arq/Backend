package pe.edu.upc.oncontrol.billing.domain.model.aggregates;

import pe.edu.upc.oncontrol.billing.domain.model.commands.plan.CreatePlanCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.plan.UpdatePlanCommand;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.CurrencyCode;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.Money;
import pe.edu.upc.oncontrol.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalDate;

@Entity
public class Plan extends AuditableAbstractAggregateRoot<Plan> {

    @Getter
    @Column(nullable = false, unique = true)
    private String name;

    @Getter
    @Embedded
    private Money price;

    @Getter
    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;

    @Getter
    @Column(name = "trial_days")
    private Integer trialDays;

    @Getter
    @Column(name = "max_patients", nullable = false)
    private int maxPatients;

    @Getter
    private boolean messagingEnabled;
    @Getter
    private boolean symptomTrackingEnabled;
    @Getter
    private boolean customRemindersEnabled;
    @Getter
    private boolean calendarIntegrationEnabled;
    @Getter
    private boolean basicReportsEnabled;
    @Getter
    private boolean advancedReportsEnabled;
    @Getter
    private int maxStorageMb;

    @Getter
    @Column(nullable = false)
    private boolean active;
    @Getter
    @Column(name="deactivated_at")
    private LocalDate deactivatedAt;

    public Plan(){}

    public Plan(String name,
                Money price,
                int durationDays,
                int trialDays,
                int maxPatients,
                boolean messagingEnabled,
                boolean symptomTrackingEnabled,
                boolean customRemindersEnabled,
                boolean calendarIntegrationEnabled,
                boolean basicReportsEnabled,
                boolean advancedReportsEnabled,
                int maxStorageMb
                ) {
        this.name = name;
        this.price = price;
        this.durationDays = durationDays;
        this.trialDays = trialDays;
        this.maxPatients = maxPatients;
        this.messagingEnabled = messagingEnabled;
        this.symptomTrackingEnabled = symptomTrackingEnabled;
        this.customRemindersEnabled = customRemindersEnabled;
        this.calendarIntegrationEnabled = calendarIntegrationEnabled;
        this.basicReportsEnabled = basicReportsEnabled;
        this.advancedReportsEnabled = advancedReportsEnabled;
        this.maxStorageMb = maxStorageMb;
        this.active = true;
        this.deactivatedAt = null;
    }

    public Plan(CreatePlanCommand command){
        this.name = command.name();
        this.price = new Money(command.priceAmount(), new CurrencyCode(command.currencyCode()));
        this.durationDays = command.durationDays();
        this.trialDays = command.trialDays();
        this.maxPatients = command.maxPatients();
        this.messagingEnabled = command.messagingEnabled();
        this.symptomTrackingEnabled = command.symptomTrackingEnabled();
        this.customRemindersEnabled = command.customRemindersEnabled();
        this.calendarIntegrationEnabled = command.calendarIntegrationEnabled();
        this.basicReportsEnabled = command.basicReportsEnabled();
        this.advancedReportsEnabled = command.advancedReportsEnabled();
        this.maxStorageMb = command.maxStorageMb();
        this.active = command.active();
        this.deactivatedAt = null;
    }

    public void deactivate(){
        this.active = false;
        this.deactivatedAt = LocalDate.now();
    }

    public void activate() {
        this.active = true;
        this.deactivatedAt = null;
    }

    public void updateDetails(UpdatePlanCommand command) {
        this.name = command.name();
    }

}
