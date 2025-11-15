package pe.edu.upc.oncontrol.billing.domain.services.paymentmethod;

import pe.edu.upc.oncontrol.billing.domain.model.commands.paymentmethod.ActivePaymentMethodCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.paymentmethod.AddPaymentMethodCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.paymentmethod.DeactivatePaymentMethodCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.paymentmethod.SetDefaultPaymentMethodCommand;
import pe.edu.upc.oncontrol.billing.domain.model.entitites.PaymentMethod;
import jakarta.servlet.http.HttpServletRequest;


public interface PaymentMethodCommandService {
    PaymentMethod add(AddPaymentMethodCommand command, HttpServletRequest request);
    void setDefault(SetDefaultPaymentMethodCommand command, HttpServletRequest request);
    void deactivate(DeactivatePaymentMethodCommand command, HttpServletRequest request);
    void active(ActivePaymentMethodCommand command);
}
