package pe.edu.upc.oncontrol.profile.interfaces.rest.transform.patient;

import pe.edu.upc.oncontrol.profile.domain.model.aggregates.PatientProfile;
import pe.edu.upc.oncontrol.profile.interfaces.rest.resources.patient.PatientProfileViewResource;

public class PatientProfileToResourceAssembler {
    public static PatientProfileViewResource toResourceFromEntity(PatientProfile profile){
        return new PatientProfileViewResource(
                profile.getUuid(),
                profile.getFullName().getFirstName(),
                profile.getFullName().getLastName(),
                profile.getContactInfo().getEmail(),
                profile.getContactInfo().getPhone(),
                profile.getBirthDate().toString(),
                profile.getGender(),
                profile.getPhotoUrl(),
                profile.isActive()
        );
    }
}
