package pe.edu.upc.oncontrol.billing.interfaces.rest.resources.suscription;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SubscriptionResource(
        Long id,
        Long adminId,
        Long planId,
        String status,
        LocalDate startDate,
        LocalDate endDate,
        boolean trialUsed,
        LocalDateTime cancelledAt
) {
}
