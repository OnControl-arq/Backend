package pe.edu.upc.oncontrol.profile.application.internal.commandservice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorProfile;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.PatientProfile;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.DeactivateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.UpdateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.patient.CreatePatientProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.patient.DeactivatePatientProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.patient.UpdatePatientProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.services.patient.PatientProfileCommandService;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.PatientProfileRepository;
import pe.edu.upc.oncontrol.shared.interfaces.acl.TokenContextFacade;

@Service
public class PatientProfileCommandServiceImpl implements PatientProfileCommandService {

    private final PatientProfileRepository patientProfileRepository;
    private final TokenContextFacade tokenContextFacade;

    public PatientProfileCommandServiceImpl(PatientProfileRepository patientProfileRepository, TokenContextFacade tokenContextFacade) {
        this.patientProfileRepository = patientProfileRepository;
        this.tokenContextFacade = tokenContextFacade;
    }

    @Override
    public PatientProfile createProfile(CreatePatientProfileCommand command, HttpServletRequest request) {
        String role = tokenContextFacade.extractUserRoleFromRequest(request);
        Long userId = "ROLE_SUPER_ADMIN".equals(role)
                ? command.userId()
                : tokenContextFacade.extractUserIdFromToken(request);

        PatientProfile profile = new PatientProfile(userId, command);
        return patientProfileRepository.save(profile);
    }

    @Override
    public PatientProfile updateProfile(UpdatePatientProfileCommand command) {
        PatientProfile profile = patientProfileRepository.findByUuid(command.patientUuid())
                .orElseThrow(() -> new EntityNotFoundException("Doctor profile not found"));

        profile.updateProfile(command);
        return patientProfileRepository.save(profile);
    }

    @Override
    public void deactivateProfile(DeactivatePatientProfileCommand command) {
        PatientProfile profile = patientProfileRepository.findByUuid(command.patientUuid())
                .orElseThrow(() -> new EntityNotFoundException("Doctor profile not found"));
        profile.deactivateProfile();
        patientProfileRepository.save(profile);
    }
}
