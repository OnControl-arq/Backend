package pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories;

import pe.edu.upc.oncontrol.billing.domain.model.entitites.SubscriptionKey;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.SubscriptionKeyCode;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.SubscriptionKeyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionKeyRepository extends JpaRepository<SubscriptionKey, Long> {
    Optional<SubscriptionKey> findByCode(SubscriptionKeyCode code);
    Boolean existsByCode(SubscriptionKeyCode code);
    List<SubscriptionKey> findAllByStatus(SubscriptionKeyStatus status);
    List<SubscriptionKey> findAllByPlanIdOrderByCreatedAtDesc(Long planId);
}
