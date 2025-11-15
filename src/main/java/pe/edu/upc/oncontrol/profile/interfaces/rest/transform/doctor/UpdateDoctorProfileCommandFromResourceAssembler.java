package pe.edu.upc.oncontrol.profile.interfaces.rest.transform.doctor;

import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.UpdateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.interfaces.rest.resources.doctor.DoctorProfileUpdateResource;

import java.util.UUID;

public class UpdateDoctorProfileCommandFromResourceAssembler {
    public static UpdateDoctorProfileCommand toCommandFromResource(UUID doctorUuid, DoctorProfileUpdateResource resource) {
        return new UpdateDoctorProfileCommand(
                doctorUuid,
                resource.firstName(),
                resource.lastName(),
                resource.email(),
                resource.phoneNumber(),
                resource.specialty(),
                resource.CMPCode(),
                resource.photoUrl()
        );
    }
}
