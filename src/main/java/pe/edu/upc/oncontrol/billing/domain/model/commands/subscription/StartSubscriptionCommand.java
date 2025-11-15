package pe.edu.upc.oncontrol.billing.domain.model.commands.subscription;

public record StartSubscriptionCommand(
        Long adminId,
        Long planId
) {
}
