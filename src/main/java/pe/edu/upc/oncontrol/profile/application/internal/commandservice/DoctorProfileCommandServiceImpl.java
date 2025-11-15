package pe.edu.upc.oncontrol.profile.application.internal.commandservice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorProfile;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.CreateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.DeactivateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.UpdateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.services.doctor.DoctorProfileCommandService;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.DoctorProfileRepository;
import pe.edu.upc.oncontrol.shared.interfaces.acl.TokenContextFacade;

@Service
public class DoctorProfileCommandServiceImpl implements DoctorProfileCommandService {
    private final DoctorProfileRepository doctorProfileRepository;
    private final TokenContextFacade tokenContextFacade;

    public DoctorProfileCommandServiceImpl(DoctorProfileRepository doctorProfileRepository, TokenContextFacade tokenContextFacade) {
        this.doctorProfileRepository = doctorProfileRepository;
        this.tokenContextFacade = tokenContextFacade;
    }

    @Override
    public DoctorProfile createProfile(CreateDoctorProfileCommand command, HttpServletRequest request) {
        String role = tokenContextFacade.extractUserRoleFromRequest(request);
        Long userId = "ROLE_SUPER_ADMIN".equals(role)
                ? command.userId()
                : tokenContextFacade.extractUserIdFromToken(request);

        DoctorProfile profile = new DoctorProfile(userId, command);
        return doctorProfileRepository.save(profile);
    }

    @Override
    public DoctorProfile updateProfile(UpdateDoctorProfileCommand command) {
        DoctorProfile profile = doctorProfileRepository.findByUuid(command.doctorUuid())
                .orElseThrow(() -> new EntityNotFoundException("Doctor profile not found"));

        profile.updateProfile(command);
        return doctorProfileRepository.save(profile);
    }

    @Override
    public void deactivateProfile(DeactivateDoctorProfileCommand command) {
        DoctorProfile profile = doctorProfileRepository.findByUuid(command.doctorUuid())
                .orElseThrow(() -> new EntityNotFoundException("Doctor profile not found"));

        profile.deactivate();
        doctorProfileRepository.save(profile);
    }
}
