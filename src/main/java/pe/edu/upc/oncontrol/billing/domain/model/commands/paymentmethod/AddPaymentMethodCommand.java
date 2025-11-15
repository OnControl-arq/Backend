package pe.edu.upc.oncontrol.billing.domain.model.commands.paymentmethod;

public record AddPaymentMethodCommand(

        Long adminId,
        String provider,
        String providerMethodId,
        String type,
        String brand,
        String last4,
        int expMonth,
        int expYear
) {}
