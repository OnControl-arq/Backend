package pe.edu.upc.oncontrol.profile.interfaces.rest.transform.link;

import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorPatientLink;
import pe.edu.upc.oncontrol.profile.interfaces.rest.resources.link.DoctorPatientLinkViewResource;

public class DoctorPatientLinkToResourceAssembler {
    public static DoctorPatientLinkViewResource toResourceFromEntity(DoctorPatientLink link){
        return new DoctorPatientLinkViewResource(
                link.getExternalId(),
                link.getDoctorProfile().getUuid(),
                link.getPatientProfile().getUuid(),
                link.getDoctorProfile().getFullName().getFullName(),
                link.getPatientProfile().getFullName().getFullName(),
                link.getStatus(),
                link.getCreatedAt(),
                link.getDisabledAt()
        );
    }
}
