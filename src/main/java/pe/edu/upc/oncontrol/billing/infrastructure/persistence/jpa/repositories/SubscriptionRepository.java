package pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Subscription;
import pe.edu.upc.oncontrol.billing.domain.model.valueobjects.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByAdminIdAndStatus(Long adminId, SubscriptionStatus status);
    boolean existsByAdminId(Long adminId);
    List<Subscription> findAllByAdminIdOrderByStartDateDesc(Long adminId);
    @Query("SELECT s FROM Subscription s WHERE s.status = 'ACTIVE'")
    List<Subscription> findAllActive();
}
