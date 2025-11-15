package pe.edu.upc.oncontrol.profile.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorProfile;
import pe.edu.upc.oncontrol.profile.domain.services.doctor.DoctorProfileQueryService;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.DoctorProfileRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class DoctorProfileQueryServiceImpl implements DoctorProfileQueryService {
    private final DoctorProfileRepository doctorProfileRepository;

    public DoctorProfileQueryServiceImpl(DoctorProfileRepository doctorProfileRepository) {
        this.doctorProfileRepository = doctorProfileRepository;
    }

    @Override
    public Optional<DoctorProfile> findByUuid(UUID uuid) {
        return doctorProfileRepository.findByUuid(uuid);
    }

    @Override
    public Optional<DoctorProfile> findByUserId(Long userId) {
        return doctorProfileRepository.findByUserId(userId);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return doctorProfileRepository.findByUserId(userId).isPresent();
    }
}
