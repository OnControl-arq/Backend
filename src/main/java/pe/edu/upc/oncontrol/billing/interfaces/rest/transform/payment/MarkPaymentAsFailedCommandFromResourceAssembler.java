package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.payment;

import pe.edu.upc.oncontrol.billing.domain.model.commands.payment.MarkPaymentAsFailedCommand;

public class MarkPaymentAsFailedCommandFromResourceAssembler {
    public static MarkPaymentAsFailedCommand toCommandFromResource(MarkPaymentAsFailedCommand resource){
        return new MarkPaymentAsFailedCommand(resource.paymentId(), resource.errorMessage());
    }
}
