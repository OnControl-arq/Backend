package pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorProfile;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {
    Optional<DoctorProfile> findByUuid(UUID uuid);
    Optional<DoctorProfile> findByUserId(Long userId);
}
