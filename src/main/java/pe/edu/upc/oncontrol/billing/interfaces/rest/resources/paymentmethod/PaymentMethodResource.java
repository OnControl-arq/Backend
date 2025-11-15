package pe.edu.upc.oncontrol.billing.interfaces.rest.resources.paymentmethod;

import jakarta.validation.constraints.*;

public record PaymentMethodResource(
        Long id,
        Long adminId,

        @NotBlank(message = "Provider cannot be blank")
        @Size(max = 20, message = "Provider cannot exceed 20 characters")
        String provider,

        @NotBlank(message = "Provider method ID cannot be blank")
        String providerMethodId,

        @NotBlank(message = "Type cannot be blank")
        @Size(max = 20, message = "Type cannot exceed 20 characters")
        String type,

        @NotBlank(message = "Brand cannot be blank")
        @Size(max = 30, message = "Brand cannot exceed 30 characters")
        String brand,

        @NotBlank(message = "Last 4 digits cannot be blank")
        @Size(max = 4, message = "Last 4 digits cannot exceed 4 characters")
        String last4,

        @NotNull(message = "Exp month cannot be null")
        @Min(value = 1, message = "Exp month must be at least 1")
        @Max(value = 12, message = "Exp month must be at most 12")
        Integer expMonth,

        @NotNull(message = "Exp year cannot be null")
        @Min(value = 2025, message = "Exp year must be at least 2025")
        @Max(value = 2099, message = "Exp year must be at most 2099")
        Integer expYear,
        boolean active
) {
}
