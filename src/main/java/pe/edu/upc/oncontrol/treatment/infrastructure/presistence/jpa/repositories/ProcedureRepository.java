package pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.Procedure;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ProcedureStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcedureRepository extends JpaRepository<Procedure, Long> {

    List<Procedure> findByTreatment_ExternalId(UUID treatmentExternalId);

    List<Procedure> findByTreatment_PatientProfileUuidAndProcedureStatus(
            UUID patientProfileUuid, ProcedureStatus status);

    List<Procedure> findByProcedureStatus(ProcedureStatus status);

    List<Procedure> findByProcedureStatusAndStartDateTimeIsNullAndCreatedAtBefore(
            ProcedureStatus procedureStatus, Date createdAt);

    List<Procedure> findAllByTreatmentExternalId(UUID treatmentExternalId);

    Optional<Procedure> findByIdAndTreatment_PatientProfileUuid(Long id, UUID patientProfileUuid);

}

