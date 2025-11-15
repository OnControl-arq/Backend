package pe.edu.upc.oncontrol.profile.application.acl;

import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorProfile;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.PatientProfile;
import pe.edu.upc.oncontrol.profile.domain.model.valueobjects.LinkStatus;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.DoctorPatientLinkRepository;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.DoctorProfileRepository;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.PatientProfileRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class ProfileAccessAcl {

    private final DoctorPatientLinkRepository linkRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final PatientProfileRepository patientProfileRepository;

    public ProfileAccessAcl(DoctorPatientLinkRepository linkRepository, DoctorProfileRepository doctorProfileRepository, PatientProfileRepository patientProfileRepository) {
        this.linkRepository = linkRepository;
        this.doctorProfileRepository = doctorProfileRepository;
        this.patientProfileRepository = patientProfileRepository;
    }

    public boolean isLinkActive(UUID doctorProfile, UUID patientProfile) {
        return linkRepository.findByDoctorProfileUuidAndPatientProfileUuid(doctorProfile, patientProfile)
                .map(link -> link.getStatus() == LinkStatus.ACTIVE)
                .orElse(false);
    }

    public Optional<UUID> getDoctorProfileUuidByUserId(Long userId) {
        return doctorProfileRepository.findByUserId(userId)
                .map(DoctorProfile::getUuid);
    }

    public Optional<UUID> getPatientProfileUuidByUserId(Long userId) {
        return patientProfileRepository.findByUserId(userId)
                .map(PatientProfile::getUuid);
    }

    public Optional<DoctorProfile> getDoctorProfileByUuid(UUID uuid) {
        return doctorProfileRepository.findByUuid(uuid);
    }

}

