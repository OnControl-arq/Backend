package pe.edu.upc.oncontrol.billing.application.internal.commandservices;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Plan;
import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Subscription;
import pe.edu.upc.oncontrol.billing.domain.model.commands.subscription.CancelSubscriptionCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.subscription.RenewSubscriptionCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.subscription.StartSubscriptionCommand;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.SubscriptionStatus;
import pe.edu.upc.oncontrol.billing.domain.services.subscription.SubscriptionCommandService;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.PlanRepository;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import pe.edu.upc.oncontrol.shared.interfaces.acl.TokenContextFacade;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final TokenContextFacade tokenContextFacade;

    public SubscriptionCommandServiceImpl(SubscriptionRepository subscriptionRepository, PlanRepository planRepository, TokenContextFacade tokenContextFacade) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.tokenContextFacade = tokenContextFacade;
    }

    @Override
    public Subscription start(StartSubscriptionCommand command, HttpServletRequest request) {
        String role = tokenContextFacade.extractUserRoleFromRequest(request);
        Long userId;
        if("ROLE_SUPER_ADMIN".equals(role)){
            userId = command.adminId();
        } else {
            userId = tokenContextFacade.extractUserIdFromToken(request);
        }
        Plan plan = planRepository.findById(command.planId())
                .filter(Plan::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        boolean hasActiveSubscription = subscriptionRepository.findByAdminIdAndStatus(userId, SubscriptionStatus.ACTIVE).isPresent();
        if(hasActiveSubscription){
            throw new IllegalArgumentException("User already has an active subscription");
        }
        boolean isNewUser = !subscriptionRepository.existsByAdminId(userId);
        boolean canUseTrial = isNewUser && plan.getTrialDays()>0;

        Subscription subscription = new Subscription(userId, plan, canUseTrial);
        return subscriptionRepository.save(subscription);
    }

    @Override
    public void cancel(CancelSubscriptionCommand command) {
        Subscription subscription = subscriptionRepository.findById(command.subscriptionId())
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));
        subscription.cancel();
        subscriptionRepository.save(subscription);
    }

    @Override
    public void forceCancel(CancelSubscriptionCommand command){
        Subscription subscription = subscriptionRepository.findById(command.subscriptionId())
                .orElseThrow(()-> new IllegalArgumentException("Subscription not found"));
        subscription.forceCancel();
        subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription renew(RenewSubscriptionCommand command) {
        Subscription subscription = subscriptionRepository.findById(command.subscriptionId())
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));
        subscription.undoCancellation();
        return subscriptionRepository.save(subscription);
    }



}
