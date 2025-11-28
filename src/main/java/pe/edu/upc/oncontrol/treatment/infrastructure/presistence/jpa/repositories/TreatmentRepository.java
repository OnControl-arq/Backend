package pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oncontrol.treatment.domain.model.aggregates.Treatment;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.TreatmentStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
    Optional<Treatment> findByExternalId(UUID externalId);
    boolean existsByDoctorProfileUuidAndPatientProfileUuidAndStatus(UUID doctorUuid, UUID patientUuid, TreatmentStatus status);
    long countByPatientProfileUuidAndStatus(UUID patientUuid, TreatmentStatus status);
    long countByDoctorProfileUuidAndPatientProfileUuidAndStatus(UUID doctorUuid, UUID patientUuid, TreatmentStatus status);
}
