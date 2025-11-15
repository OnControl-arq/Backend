package pe.edu.upc.oncontrol.billing.domain.model.commands.subscriptionkey;

public record UseSubscriptionKeyCommand(Long subscriptionKeyId, Long userId) {}
