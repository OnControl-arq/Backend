package pe.edu.upc.oncontrol.profile.interfaces.rest.transform.doctor;

import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorProfile;
import pe.edu.upc.oncontrol.profile.interfaces.rest.resources.doctor.DoctorProfileViewResource;

public class DoctorProfileToResourceAssembler {
    public static DoctorProfileViewResource toResourceFromEntity(DoctorProfile profile){
        return new DoctorProfileViewResource(
                profile.getUuid(),
                profile.getFullName().getFirstName(),
                profile.getFullName().getLastName(),
                profile.getContactInfo().getEmail(),
                profile.getContactInfo().getPhone(),
                profile.getDocumentIdent().getType(),
                profile.getDocumentIdent().getNumber(),
                profile.getSpecialty(),
                profile.getCMPCode(),
                profile.getPhotoUrl(),
                profile.isActive()
        );
    }
}
