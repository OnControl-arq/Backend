package pe.edu.upc.oncontrol.profile.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorPatientLink;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorProfile;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.PatientProfile;
import pe.edu.upc.oncontrol.profile.domain.model.valueobjects.LinkStatus;
import pe.edu.upc.oncontrol.profile.domain.services.link.DoctorPatientLinkQueryService;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.DoctorPatientLinkRepository;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.DoctorProfileRepository;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.PatientProfileRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DoctorPatientLinkQueryServiceImpl implements DoctorPatientLinkQueryService {

    private final DoctorPatientLinkRepository doctorPatientLinkRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final PatientProfileRepository patientProfileRepository;

    public DoctorPatientLinkQueryServiceImpl(DoctorPatientLinkRepository doctorPatientLinkRepository, DoctorProfileRepository doctorProfileRepository, PatientProfileRepository patientProfileRepository) {
        this.doctorPatientLinkRepository = doctorPatientLinkRepository;
        this.doctorProfileRepository = doctorProfileRepository;
        this.patientProfileRepository = patientProfileRepository;
    }

    @Override
    public Optional<DoctorPatientLink> findByExternalId(UUID externalId) {
        return doctorPatientLinkRepository.findByExternalId(externalId);
    }

    @Override
    public Optional<DoctorPatientLink> findByDoctorUuidAndPatientUuid(UUID doctorUuid, UUID patientUuid) {
        Optional<DoctorProfile> doctorOpt = doctorProfileRepository.findByUuid(doctorUuid);
        Optional<PatientProfile> patientOpt = patientProfileRepository.findByUuid(patientUuid);

        if (doctorOpt.isEmpty() || patientOpt.isEmpty()) {
            return Optional.empty();
        }

        return doctorPatientLinkRepository.findByDoctorProfileAndPatientProfile(
                doctorOpt.get(), patientOpt.get()
        );
    }

    @Override
    public List<DoctorPatientLink> findAllByDoctorUuidAndStatus(UUID doctorUuid, LinkStatus status) {
        return doctorProfileRepository.findByUuid(doctorUuid)
                .map(doctor -> doctorPatientLinkRepository.findAllByDoctorProfileAndStatus(doctor, status))
                .orElse(List.of());
    }

    @Override
    public List<DoctorPatientLink> findAllByPatientUuidAndStatus(UUID patientUuid, LinkStatus status) {
        return patientProfileRepository.findByUuid(patientUuid)
                .map(patient -> doctorPatientLinkRepository.findAllByPatientProfileAndStatus(patient, status))
                .orElse(List.of());
    }

}
