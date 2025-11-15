package pe.edu.upc.oncontrol.billing.domain.model.commands.subscriptionkey;

public record CreateKeyCommand(
        String status,
        Long planId,
        Integer quantity
) {}
