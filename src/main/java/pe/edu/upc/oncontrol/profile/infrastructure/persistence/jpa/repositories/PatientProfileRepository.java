package pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.PatientProfile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfile, Long> {
    Optional<PatientProfile> findByUuid(UUID uuid);
    Optional<PatientProfile> findByUserId(Long userId);
    @Query("SELECT p FROM PatientProfile p WHERE " +
            "LOWER(p.fullName.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(p.fullName.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<PatientProfile> searchByFullName(@Param("name") String nameFragment);
    Optional<PatientProfile> findByContactInfoEmailIgnoreCase(String email);

}
