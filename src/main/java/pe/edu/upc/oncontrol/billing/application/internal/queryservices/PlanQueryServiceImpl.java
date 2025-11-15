package pe.edu.upc.oncontrol.billing.application.internal.queryservices;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Plan;
import pe.edu.upc.oncontrol.billing.domain.services.plan.PlanQueryService;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.PlanRepository;
import pe.edu.upc.oncontrol.shared.interfaces.acl.TokenContextFacade;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanQueryServiceImpl implements PlanQueryService {

    private final PlanRepository planRepository;
    private final TokenContextFacade tokenContextFacade;

    public PlanQueryServiceImpl(PlanRepository planRepository, TokenContextFacade tokenContextFacade) {
        this.planRepository = planRepository;
        this.tokenContextFacade = tokenContextFacade;
    }
    @Override
    public Optional<Plan> getById(Long planId, HttpServletRequest request) {
        String role = tokenContextFacade.extractUserRoleFromRequest(request);
        return planRepository.findById(planId)
                .filter(plan -> "ROLE_SUPER_ADMIN".equals(role) || plan.isActive());
    }

    @Override
    public List<Plan> getAccessiblePlans(HttpServletRequest request) {
        String role = tokenContextFacade.extractUserRoleFromRequest(request);
        if ("ROLE_SUPER_ADMIN".equals(role)) {
            return planRepository.findAll();
        }
        return planRepository.findAllByActiveTrue();
    }
}
