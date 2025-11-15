package pe.edu.upc.oncontrol.profile.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.PatientProfile;
import pe.edu.upc.oncontrol.profile.domain.services.patient.PatientProfileQueryService;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.PatientProfileRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientProfileQueryServiceImpl implements PatientProfileQueryService {
    private final PatientProfileRepository patientProfileRepository;

    public PatientProfileQueryServiceImpl(PatientProfileRepository patientProfileRepository) {
        this.patientProfileRepository = patientProfileRepository;
    }

    @Override
    public Optional<PatientProfile> findByUuid(UUID uuid) {
        return patientProfileRepository.findByUuid(uuid);

    }

    @Override
    public Optional<PatientProfile> findByUserId(Long userId) {
        return patientProfileRepository.findByUserId(userId);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return patientProfileRepository.findByUserId(userId).isPresent();
    }

    @Override
    public List<PatientProfile> searchByFullName(String nameFragment) {
        return patientProfileRepository.searchByFullName(nameFragment);
    }

    @Override
    public Optional<PatientProfile> findByEmail(String email) {
        return patientProfileRepository.findByContactInfoEmailIgnoreCase(email);
    }


}
