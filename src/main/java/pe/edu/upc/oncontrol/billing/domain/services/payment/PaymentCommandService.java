package pe.edu.upc.oncontrol.billing.domain.services.payment;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Payment;
import pe.edu.upc.oncontrol.billing.domain.model.commands.payment.MarkPaymentAsFailedCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.payment.MarkPaymentAsPaidCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.payment.RegisterPaymentCommand;

public interface PaymentCommandService {
    Payment register(RegisterPaymentCommand command);
    Payment markAsPaid(MarkPaymentAsPaidCommand command);
    Payment markAsFailed(MarkPaymentAsFailedCommand command);
}
