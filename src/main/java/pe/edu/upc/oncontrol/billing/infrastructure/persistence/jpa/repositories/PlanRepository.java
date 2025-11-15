package pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories;

import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findAllByActiveTrue();
}
