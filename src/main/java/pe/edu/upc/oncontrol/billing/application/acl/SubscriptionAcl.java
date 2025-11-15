package pe.edu.upc.oncontrol.billing.application.acl;

import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.billing.application.internal.queryservices.SubscriptionQueryServiceImpl;
import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Plan;
import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Subscription;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.PlanRepository;
import pe.edu.upc.oncontrol.profile.application.acl.ProfileAccessAcl;

import java.util.Optional;
import java.util.UUID;

@Component
public class SubscriptionAcl {

    private final SubscriptionQueryServiceImpl subscriptionQueryService;
    private final PlanRepository planRepository;
    private final ProfileAccessAcl profileAccessAcl;

    public SubscriptionAcl(SubscriptionQueryServiceImpl subscriptionQueryService, PlanRepository planRepository, ProfileAccessAcl profileAccessAcl) {
        this.subscriptionQueryService = subscriptionQueryService;
        this.planRepository = planRepository;
        this.profileAccessAcl = profileAccessAcl;
    }

    public Optional<Plan> getActivePlanByAdminId(Long adminId) {
        Optional<Subscription> subscription = subscriptionQueryService.getActiveByAdminId(adminId);
        return subscription.flatMap(sub -> planRepository.findById(sub.getPlan().getId()));
    }

    public Optional<Plan> getActivePlanByAdminIdFromUuid(UUID doctorUuid) {
        return profileAccessAcl.getDoctorProfileByUuid(doctorUuid)
                .flatMap(profile -> getActivePlanByAdminId(profile.getUserId()));
    }


}