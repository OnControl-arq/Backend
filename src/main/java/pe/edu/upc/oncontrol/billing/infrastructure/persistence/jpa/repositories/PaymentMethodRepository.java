package pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories;

import pe.edu.upc.oncontrol.billing.domain.model.entitites.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    List<PaymentMethod> findAllByAdminId(Long adminId);
    Optional<PaymentMethod> findByAdminIdAndActiveTrueAndDefaultMethodTrue(Long adminId);
    List<PaymentMethod> findAllByActiveTrue();
    @Query(""" 
    SELECT pm FROM PaymentMethod pm
    WHERE pm.active = true
        AND (pm.expYear < :year OR (pm.expYear = :year AND pm.expMonth <= :month))""")
    List<PaymentMethod> findExpiredMethods(@Param("year") int year, @Param("month") int month);

    List<PaymentMethod> findByActiveFalseAndDeactivatedAtBefore(LocalDateTime dateTime);
}
