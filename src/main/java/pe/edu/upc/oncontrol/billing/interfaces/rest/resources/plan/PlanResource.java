package pe.edu.upc.oncontrol.billing.interfaces.rest.resources.plan;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PlanResource(

       Long id,
       @NotBlank(message = "Plan name cannot be blank")
       @Size(max = 100, message = "Plan name cannot exceed 100 characters")
       String name,

       @NotNull(message = "Price amount cannot be null")
       @Min(value = 1, message = "Price amount must be greater than 0")
       BigDecimal priceAmount,

       @NotBlank(message = "Currency code cannot be blank")
       String currencyCode,

       @NotNull(message = "Price currency code cannot be null")
       @Min(value = 15, message = "Duration days must be greater than 0")
       Integer durationDays,

       @Min(value = 0, message = "Trial days must be greater than or equal to 0")
       Integer trialDays,

       @NotNull(message = "Max patients cannot be null")
       @Min(value = 5, message = "Max patients must be at least 5")
       Integer maxPatients,

       boolean messagingEnabled,
       boolean symptomTrackingEnabled,
       boolean customRemindersEnabled,
       boolean calendarIntegrationEnabled,
       boolean basicReportsEnabled,
       boolean advancedReportsEnabled,

       @Min(value = 0, message = "Storage must be zero or more")
       Integer maxStorageMb,
       boolean active
) {
}
