package pe.edu.upc.oncontrol.billing.domain.model.commands.payment;

import java.math.BigDecimal;

public record RegisterPaymentCommand(
        Long subscriptionId,
        BigDecimal amount,
        String currencyCode,
        String provider,
        String providerPaymentId
) {}
