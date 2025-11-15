package pe.edu.upc.oncontrol.profile.interfaces.rest.transform.doctor;

import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.CreateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.interfaces.rest.resources.doctor.DoctorProfileCreateResource;

public class CreateDoctorProfileCommandFromResourceAssembler {
    public static CreateDoctorProfileCommand toCommandFromResource(DoctorProfileCreateResource resource){
        return new CreateDoctorProfileCommand(
                resource.userId(),
                resource.firstName(),
                resource.lastName(),
                resource.email(),
                resource.phoneNumber(),
                resource.documentType(),
                resource.documentNumber(),
                resource.specialty(),
                resource.CMPCode(),
                resource.photoUrl()
        );
    }
}
