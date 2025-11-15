package pe.edu.upc.oncontrol.billing.domain.services.subscriptionKey;

import pe.edu.upc.oncontrol.billing.domain.model.entitites.SubscriptionKey;
import pe.edu.upc.oncontrol.billing.domain.model.entitites.SubscriptionKeyUsage;
import pe.edu.upc.oncontrol.billing.domain.model.queries.subscriptionKey.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public interface SubscriptionKeyQueryService {
    Optional<SubscriptionKey> getById(FindSubscriptionKeyByIdQuery query);
    Optional<SubscriptionKey> getByCode(FindSubscriptionKeyByCodeQuery query);
    List<SubscriptionKey> getByStatus(FindSubscriptionKeysByStatusQuery query);
    List<SubscriptionKey> getByPlanId(FindSubscriptionKeysByPlanIdQuery  query);
    List<SubscriptionKeyUsage> getUsageByUserId(FindActiveUsageByUserIdQuery query, HttpServletRequest request);
    List<SubscriptionKeyUsage> getUsageByKey(FindUsageBySubscriptionKeyIdQuery query);
}
