package pe.edu.upc.oncontrol.profile.domain.services.doctor;

import jakarta.servlet.http.HttpServletRequest;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorProfile;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.CreateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.DeactivateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.UpdateDoctorProfileCommand;

public interface DoctorProfileCommandService {
    DoctorProfile createProfile(CreateDoctorProfileCommand command, HttpServletRequest request);
    DoctorProfile updateProfile(UpdateDoctorProfileCommand command);
    void deactivateProfile(DeactivateDoctorProfileCommand command);
}
