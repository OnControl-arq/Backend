package pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.ProcedureExecution;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcedureExecutionRepository extends JpaRepository<ProcedureExecution, Long> {
    List<ProcedureExecution> findByProcedure_Id(Long procedureId);

    Optional<ProcedureExecution> findByIdAndProcedure_Treatment_PatientProfileUuid(
            Long executionId, UUID patientProfileUuid
    );

    @Query("SELECT e FROM ProcedureExecution e WHERE e.status = 'PENDING' AND e.window.scheduledAt < :threshold")
    List<ProcedureExecution> findPendingExecutionsOutsideGracePeriod(@Param("threshold") LocalDateTime threshold);

    @Query("SELECT COUNT(e) = 0 FROM ProcedureExecution  e WHERE e.procedure.id = :procedureId AND e.status = 'PENDING'")
    boolean isProcedureFullyCompleted(@Param("procedureId") Long procedureId);

}
