package pe.edu.upc.oncontrol.profile.interfaces.rest.transform.link;

import pe.edu.upc.oncontrol.profile.domain.model.commands.link.CreateLinkCommand;
import pe.edu.upc.oncontrol.profile.interfaces.rest.resources.link.CreateLinkResource;

public class CreateLinkCommandFromResourceAssembler {
    public static CreateLinkCommand toCommandFromResource(CreateLinkResource resource){
        return new CreateLinkCommand(
                resource.doctorUuid(),
                resource.patientUuid()
        );
    }
}
