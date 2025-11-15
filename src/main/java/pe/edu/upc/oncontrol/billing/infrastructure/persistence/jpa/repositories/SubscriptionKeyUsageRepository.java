package pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories;

import pe.edu.upc.oncontrol.billing.domain.model.entitites.SubscriptionKeyUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionKeyUsageRepository extends JpaRepository<SubscriptionKeyUsage, Long> {
    List<SubscriptionKeyUsage> findAllByUserId(Long userId);
    List<SubscriptionKeyUsage> findAllBySubscriptionKeyId(Long subscriptionKeyId);
}
