package pe.edu.upc.oncontrol.billing.application.internal.queryservices;

import pe.edu.upc.oncontrol.billing.domain.model.entitites.SubscriptionKey;
import pe.edu.upc.oncontrol.billing.domain.model.entitites.SubscriptionKeyUsage;
import pe.edu.upc.oncontrol.billing.domain.model.queries.subscriptionKey.*;
import pe.edu.upc.oncontrol.billing.domain.services.subscriptionKey.SubscriptionKeyQueryService;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.SubscriptionKeyRepository;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.SubscriptionKeyUsageRepository;
import pe.edu.upc.oncontrol.shared.interfaces.acl.TokenContextFacade;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class SubscriptionKeyQueryServiceImpl implements SubscriptionKeyQueryService {

    private final SubscriptionKeyRepository subscriptionKeyRepository;
    private final SubscriptionKeyUsageRepository subscriptionKeyUsageRepository;
    private final TokenContextFacade tokenContextFacade;

    public SubscriptionKeyQueryServiceImpl(SubscriptionKeyRepository subscriptionKeyRepository, SubscriptionKeyUsageRepository subscriptionKeyUsageRepository, TokenContextFacade tokenContextFacade) {
        this.subscriptionKeyRepository = subscriptionKeyRepository;
        this.subscriptionKeyUsageRepository = subscriptionKeyUsageRepository;
        this.tokenContextFacade = tokenContextFacade;
    }


    @Override
    public Optional<SubscriptionKey> getById(FindSubscriptionKeyByIdQuery query) {
        return subscriptionKeyRepository.findById(query.subscriptionKeyId());
    }

    @Override
    public Optional<SubscriptionKey> getByCode(FindSubscriptionKeyByCodeQuery query) {
        return subscriptionKeyRepository.findByCode(query.code());
    }

    @Override
    public List<SubscriptionKey> getByStatus(FindSubscriptionKeysByStatusQuery query) {
        return subscriptionKeyRepository.findAllByStatus(query.status());
    }

    @Override
    public List<SubscriptionKey> getByPlanId(FindSubscriptionKeysByPlanIdQuery query) {
        return subscriptionKeyRepository.findAllByPlanIdOrderByCreatedAtDesc(query.planId());
    }

    @Override
    public List<SubscriptionKeyUsage> getUsageByUserId(FindActiveUsageByUserIdQuery query, HttpServletRequest request) {
        String role = tokenContextFacade.extractUserRoleFromRequest(request);
        Long tokenUserId = tokenContextFacade.extractUserIdFromToken(request);

        if (!"ROLE_SUPER_ADMIN".equals(role) && !tokenUserId.equals(query.userId())) {
            throw new SecurityException("Admins can only view their own key usage");
        }

        return subscriptionKeyUsageRepository.findAllByUserId(query.userId());
    }

    @Override
    public List<SubscriptionKeyUsage> getUsageByKey(FindUsageBySubscriptionKeyIdQuery query) {
        return subscriptionKeyUsageRepository.findAllBySubscriptionKeyId(query.subscriptionKeyId());
    }
}
