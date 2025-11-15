package pe.edu.upc.oncontrol.billing.domain.services.subscription;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Subscription;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public interface SubscriptionQueryService {
    Optional<Subscription> getById(Long subscriptionId, HttpServletRequest request);
    Optional<Subscription> getActiveByAdminId(Long adminId, HttpServletRequest request);
    List<Subscription> getHistoryByAdminId(Long adminId, HttpServletRequest request);
    Optional<Subscription> getActiveByAdminId(Long adminId);
}
