package pe.edu.upc.oncontrol.billing.domain.model.commands.plan;

import java.math.BigDecimal;

public record CreatePlanCommand(
        String name,
        BigDecimal priceAmount,
        String currencyCode,
        int durationDays,
        int trialDays,
        int maxPatients,
        boolean messagingEnabled,
        boolean symptomTrackingEnabled,
        boolean customRemindersEnabled,
        boolean calendarIntegrationEnabled,
        boolean basicReportsEnabled,
        boolean advancedReportsEnabled,
        int maxStorageMb,
        boolean active
) {}

