package pe.edu.upc.oncontrol.profile.domain.services.doctor;

import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorProfile;

import java.util.Optional;
import java.util.UUID;

public interface DoctorProfileQueryService {
    Optional<DoctorProfile> findByUuid(UUID uuid);
    Optional<DoctorProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
