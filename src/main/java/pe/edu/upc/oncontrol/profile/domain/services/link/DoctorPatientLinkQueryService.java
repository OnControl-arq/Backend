package pe.edu.upc.oncontrol.profile.domain.services.link;

import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorPatientLink;
import pe.edu.upc.oncontrol.profile.domain.model.valueobjects.LinkStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorPatientLinkQueryService {
    Optional<DoctorPatientLink> findByExternalId(UUID externalId);
    Optional<DoctorPatientLink> findByDoctorUuidAndPatientUuid(UUID doctorUuid, UUID patientUuid);
    List<DoctorPatientLink> findAllByDoctorUuidAndStatus(UUID doctorUuid, LinkStatus status);
    List<DoctorPatientLink> findAllByPatientUuidAndStatus(UUID patientUuid, LinkStatus status);
}
