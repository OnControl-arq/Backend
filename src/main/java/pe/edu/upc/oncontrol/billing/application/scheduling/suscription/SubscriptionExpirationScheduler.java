package pe.edu.upc.oncontrol.billing.application.scheduling.suscription;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Subscription;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionExpirationScheduler {
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionExpirationScheduler(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }
    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void expireSubscriptions(){
        List<Subscription> activeSubscriptions = subscriptionRepository.findAllActive();
        LocalDate today = LocalDate.now();
        for(Subscription sub : activeSubscriptions){
            if(sub.getEndDate().isBefore(today) || sub.getEndDate().isEqual(today)){
                if(sub.getCancelledAt() != null){
                    sub.finalizeCancellation();
                } else {
                    sub.expire();
                }
                subscriptionRepository.save(sub);
            }
        }
    }

}
