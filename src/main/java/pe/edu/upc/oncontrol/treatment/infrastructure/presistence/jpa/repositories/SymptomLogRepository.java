package pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.SymptomLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SymptomLogRepository extends JpaRepository<SymptomLog, Long> {

    List<SymptomLog> findByTreatment_ExternalIdOrderByLoggedAtDesc(UUID treatmentExternalId);
    List<SymptomLog> findByTreatment_PatientProfileUuidOrderByLoggedAtDesc(UUID patientProfileUuid);
    @Query("SELECT s FROM SymptomLog s WHERE s.loggedAt BETWEEN :start AND :end AND s.treatment.patientProfileUuid = :patientUuid")
    List<SymptomLog> findLogsInRangeByPatient(@Param("patientUuid") UUID patientUuid,
                                              @Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end);

}

