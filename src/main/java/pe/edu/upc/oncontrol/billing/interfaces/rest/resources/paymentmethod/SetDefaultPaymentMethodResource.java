package pe.edu.upc.oncontrol.billing.interfaces.rest.resources.paymentmethod;

import jakarta.validation.constraints.NotNull;

public record SetDefaultPaymentMethodResource(
        @NotNull(message = "Payment method Id is required")
        Long paymentMethodId
) {}
