package pe.edu.upc.oncontrol.billing.interfaces.rest.resources.paymentmethod;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ActivePaymentMethodCommandResource(
        @NotNull(message = "Payment method Id is required")
        @Min(value = 1, message = "Payment method Id invalid")
        Long paymentMethodId
) {
}
