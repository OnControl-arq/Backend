package pe.edu.upc.oncontrol.billing.domain.services.subscription;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Subscription;
import pe.edu.upc.oncontrol.billing.domain.model.commands.subscription.CancelSubscriptionCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.subscription.RenewSubscriptionCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.subscription.StartSubscriptionCommand;
import jakarta.servlet.http.HttpServletRequest;

public interface SubscriptionCommandService {
    Subscription start(StartSubscriptionCommand command, HttpServletRequest request);
    void cancel(CancelSubscriptionCommand command);
    void forceCancel(CancelSubscriptionCommand command);
    Subscription renew(RenewSubscriptionCommand command);
}
