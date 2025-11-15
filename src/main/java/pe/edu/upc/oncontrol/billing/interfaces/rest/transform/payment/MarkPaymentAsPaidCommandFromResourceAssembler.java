package pe.edu.upc.oncontrol.billing.interfaces.rest.transform.payment;

import pe.edu.upc.oncontrol.billing.domain.model.commands.payment.MarkPaymentAsPaidCommand;

public class MarkPaymentAsPaidCommandFromResourceAssembler {
    public static MarkPaymentAsPaidCommand toCommandFromResource(MarkPaymentAsPaidCommand resource){
        return new MarkPaymentAsPaidCommand(resource.paymentId());
    }
}
