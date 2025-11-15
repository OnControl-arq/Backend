package pe.edu.upc.oncontrol.billing.domain.services.subscriptionKey;

import pe.edu.upc.oncontrol.billing.domain.model.commands.subscriptionkey.ActivateSubscriptionKeyCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.subscriptionkey.CreateKeyCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.subscriptionkey.DeactivateSubscriptionKeyCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.subscriptionkey.UseSubscriptionKeyCommand;
import pe.edu.upc.oncontrol.billing.domain.model.entitites.SubscriptionKey;
import pe.edu.upc.oncontrol.billing.domain.model.entitites.SubscriptionKeyUsage;
import jakarta.servlet.http.HttpServletRequest;

public interface SubscriptionKeyCommandService {
    SubscriptionKey createKey(CreateKeyCommand command);
    void deactivateKey(DeactivateSubscriptionKeyCommand command);
    void activateKey(ActivateSubscriptionKeyCommand command);
    SubscriptionKeyUsage useKey(UseSubscriptionKeyCommand command, HttpServletRequest request);

}
