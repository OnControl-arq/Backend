package pe.edu.upc.oncontrol.billing.interfaces.rest.resources.payment;

import java.math.BigDecimal;

public record PaymentResource(
        Long subscriptionId,
        BigDecimal amount,
        String currencyCode,
        String provider,
        String providerPaymentId
) {}
