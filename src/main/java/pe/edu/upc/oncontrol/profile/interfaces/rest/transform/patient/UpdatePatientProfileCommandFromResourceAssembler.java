package pe.edu.upc.oncontrol.profile.interfaces.rest.transform.patient;

import pe.edu.upc.oncontrol.profile.domain.model.commands.patient.UpdatePatientProfileCommand;
import pe.edu.upc.oncontrol.profile.interfaces.rest.resources.patient.PatientProfileUpdateResource;

import java.util.UUID;

public class UpdatePatientProfileCommandFromResourceAssembler {
    public static UpdatePatientProfileCommand toCommandFromResource(UUID patientUuid, PatientProfileUpdateResource resource){
        return new UpdatePatientProfileCommand(
                patientUuid,
                resource.firstName(),
                resource.lastName(),
                resource.email(),
                resource.phoneNumber(),
                resource.birthDate(),
                resource.photoUrl()
        );
    }
}
