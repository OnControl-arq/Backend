package pe.edu.upc.oncontrol.billing.application.internal.commandservices;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Plan;
import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Subscription;
import pe.edu.upc.oncontrol.billing.domain.model.commands.subscriptionkey.ActivateSubscriptionKeyCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.subscriptionkey.CreateKeyCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.subscriptionkey.DeactivateSubscriptionKeyCommand;
import pe.edu.upc.oncontrol.billing.domain.model.commands.subscriptionkey.UseSubscriptionKeyCommand;
import pe.edu.upc.oncontrol.billing.domain.model.entitites.SubscriptionKey;
import pe.edu.upc.oncontrol.billing.domain.model.entitites.SubscriptionKeyUsage;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.SubscriptionKeyCode;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.SubscriptionKeyStatus;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.SubscriptionStatus;
import pe.edu.upc.oncontrol.billing.domain.services.subscriptionKey.SubscriptionKeyCommandService;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.PlanRepository;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.SubscriptionKeyRepository;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.SubscriptionKeyUsageRepository;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import pe.edu.upc.oncontrol.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.edu.upc.oncontrol.shared.interfaces.acl.TokenContextFacade;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SubscriptionKeyCommandServiceImpl implements SubscriptionKeyCommandService {

    final private SubscriptionKeyRepository subscriptionKeyRepository;
    final private SubscriptionKeyUsageRepository subscriptionKeyUsageRepository;
    final private PlanRepository planRepository;
    final private SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final TokenContextFacade tokenContextFacade;

    public SubscriptionKeyCommandServiceImpl(SubscriptionKeyRepository subscriptionKeyRepository, SubscriptionKeyUsageRepository subscriptionKeyUsageRepository, PlanRepository planRepository, SubscriptionRepository subscriptionRepository, UserRepository userRepository, TokenContextFacade tokenContextFacade) {
        this.subscriptionKeyRepository = subscriptionKeyRepository;
        this.subscriptionKeyUsageRepository = subscriptionKeyUsageRepository;
        this.planRepository = planRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.tokenContextFacade = tokenContextFacade;
    }

    @Override
    public SubscriptionKey createKey(CreateKeyCommand command) {
        Plan plan = planRepository.findById(command.planId())
                .orElseThrow(() -> new IllegalThreadStateException("Plan not found"));
        SubscriptionKeyCode code = SubscriptionKeyCode.random();
        if(subscriptionKeyRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Key code already exists, please try again");
        }
        SubscriptionKey newKey = new SubscriptionKey(
                command.status(),
                plan,
                command.quantity(),
                code
        );
        return subscriptionKeyRepository.save(newKey);
    }

    @Override
    public void deactivateKey(DeactivateSubscriptionKeyCommand command) {
        SubscriptionKey key = subscriptionKeyRepository.findById(command.subscriptionKeyId())
                .orElseThrow(()-> new IllegalThreadStateException("Key not found"));
        key.deactivate();
        subscriptionKeyRepository.save(key);
    }

    @Override
    public void activateKey(ActivateSubscriptionKeyCommand command){
        SubscriptionKey key = subscriptionKeyRepository.findById(command.subscriptionKeyId())
                .orElseThrow(()-> new IllegalThreadStateException("Key not found"));
        key.activate();
        subscriptionKeyRepository.save(key);
    }

    @Override
    public SubscriptionKeyUsage useKey(UseSubscriptionKeyCommand command, HttpServletRequest request) {
        String role = tokenContextFacade.extractUserRoleFromRequest(request);
        Long userId = "ROLE_SUPER_ADMIN".equals(role)
                ? command.userId()
                : tokenContextFacade.extractUserIdFromToken(request);

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        boolean hasActiveSubscription = subscriptionRepository
                .findByAdminIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .isPresent();

        if(hasActiveSubscription){
            throw new IllegalArgumentException("User already has an active subscription");
        }

        SubscriptionKey key = subscriptionKeyRepository.findById(command.subscriptionKeyId())
                .orElseThrow(()-> new IllegalArgumentException("Key not found"));

        if(key.getStatus() != SubscriptionKeyStatus.ACTIVE || key.getQuantity() <= 0) {
            throw new IllegalArgumentException("Key is not active or has no remaining uses");
        }

        Subscription subscription = new Subscription(
                userId,
                key.getPlan(),
                key.getDurationDays()
        );
        subscriptionRepository.save(subscription);

        SubscriptionKeyUsage usage = new SubscriptionKeyUsage(key, userId, LocalDateTime.now());
        key.useOnce();

        subscriptionKeyRepository.save(key);
        return subscriptionKeyUsageRepository.save(usage);
    }
}
