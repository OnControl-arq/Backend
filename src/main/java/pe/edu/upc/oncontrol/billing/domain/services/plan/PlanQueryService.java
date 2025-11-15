package pe.edu.upc.oncontrol.billing.domain.services.plan;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Plan;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public interface PlanQueryService {
    Optional<Plan> getById(Long planId, HttpServletRequest request);
    List<Plan> getAccessiblePlans(HttpServletRequest request);
}
