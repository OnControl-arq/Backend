package pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorPatientLink;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorProfile;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.PatientProfile;
import pe.edu.upc.oncontrol.profile.domain.model.valueobjects.LinkStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorPatientLinkRepository extends JpaRepository<DoctorPatientLink, Long> {
    Optional<DoctorPatientLink> findByExternalId(UUID externalId);
    Optional<DoctorPatientLink> findByDoctorProfileAndPatientProfile(DoctorProfile doctor, PatientProfile patient);
    Optional<DoctorPatientLink> findByDoctorProfileUuidAndPatientProfileUuid(UUID doctorProfileUuid, UUID patientProfileUuid);
    List<DoctorPatientLink> findAllByDoctorProfileAndStatus(DoctorProfile doctor, LinkStatus status);
    List<DoctorPatientLink> findAllByPatientProfileAndStatus(PatientProfile patient, LinkStatus status);
    int countByDoctorProfileAndStatus(DoctorProfile doctor, LinkStatus status);

}
