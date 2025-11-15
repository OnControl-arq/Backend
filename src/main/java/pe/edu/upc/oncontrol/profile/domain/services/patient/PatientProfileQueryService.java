package pe.edu.upc.oncontrol.profile.domain.services.patient;

import pe.edu.upc.oncontrol.profile.domain.model.aggregates.PatientProfile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientProfileQueryService {
    Optional<PatientProfile> findByUuid(UUID uuid);
    Optional<PatientProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    List<PatientProfile> searchByFullName(String nameFragment);
    Optional<PatientProfile> findByEmail(String email);
}
