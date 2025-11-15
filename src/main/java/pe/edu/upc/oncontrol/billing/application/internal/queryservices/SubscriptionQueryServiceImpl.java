package pe.edu.upc.oncontrol.billing.application.internal.queryservices;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Subscription;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.SubscriptionStatus;
import pe.edu.upc.oncontrol.billing.domain.services.subscription.SubscriptionQueryService;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import pe.edu.upc.oncontrol.shared.interfaces.acl.TokenContextFacade;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionQueryServiceImpl implements SubscriptionQueryService {

    private final SubscriptionRepository subscriptionRepository;
    private final TokenContextFacade tokenContextFacade;

    public SubscriptionQueryServiceImpl(SubscriptionRepository subscriptionRepository, TokenContextFacade tokenContextFacade) {
        this.subscriptionRepository = subscriptionRepository;
        this.tokenContextFacade = tokenContextFacade;
    }

    @Override
    public Optional<Subscription> getById(Long subscriptionId, HttpServletRequest request) {
        String role = tokenContextFacade.extractUserRoleFromRequest(request);
        Long userId = tokenContextFacade.extractUserIdFromToken(request);

        return subscriptionRepository.findById(subscriptionId)
                .filter(subscription ->
                        "ROLE_SUPER_ADMIN".equals(role) || subscription.getAdminId().equals(userId)
                );
    }

    @Override
    public Optional<Subscription> getActiveByAdminId(Long adminId, HttpServletRequest request) {
        String role = tokenContextFacade.extractUserRoleFromRequest(request);
        Long userId = tokenContextFacade.extractUserIdFromToken(request);

        if (!"ROLE_SUPER_ADMIN".equals(role) && !adminId.equals(userId)) {
            throw new SecurityException("Not authorized to view this subscription");
        }
        return subscriptionRepository.findByAdminIdAndStatus(adminId, SubscriptionStatus.ACTIVE);
    }

    @Override
    public List<Subscription> getHistoryByAdminId(Long adminId, HttpServletRequest request) {
        String role = tokenContextFacade.extractUserRoleFromRequest(request);
        Long userId = tokenContextFacade.extractUserIdFromToken(request);

        if (!"ROLE_SUPER_ADMIN".equals(role) && !adminId.equals(userId)) {
            throw new SecurityException("Not authorized to view subscription history");
        }
        return subscriptionRepository.findAllByAdminIdOrderByStartDateDesc(adminId);

    }
    @Override
    public Optional<Subscription> getActiveByAdminId(Long adminId) {
        return subscriptionRepository.findByAdminIdAndStatus(adminId, SubscriptionStatus.ACTIVE);
    }
}
