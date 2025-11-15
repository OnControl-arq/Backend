package pe.edu.upc.oncontrol.profile.interfaces.rest.transform.patient;

import pe.edu.upc.oncontrol.profile.domain.model.commands.patient.CreatePatientProfileCommand;
import pe.edu.upc.oncontrol.profile.interfaces.rest.resources.patient.PatientProfileCreateResource;

public class CreatePatientProfileCommandFromResourceAssembler {
    public static CreatePatientProfileCommand toCommandFromResource(PatientProfileCreateResource resource){
        return new CreatePatientProfileCommand(
                resource.userId(),
                resource.firstName(),
                resource.lastName(),
                resource.email(),
                resource.phoneNumber(),
                resource.birthDate(),
                resource.gender(),
                resource.photoUrl()
        );
    }
}
