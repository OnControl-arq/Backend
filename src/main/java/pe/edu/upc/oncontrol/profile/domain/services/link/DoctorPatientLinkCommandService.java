package pe.edu.upc.oncontrol.profile.domain.services.link;

import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorPatientLink;
import pe.edu.upc.oncontrol.profile.domain.model.commands.link.*;

public interface DoctorPatientLinkCommandService {
    DoctorPatientLink createLink(CreateLinkCommand command);
    void acceptLink(AcceptLinkCommand command);
    void rejectLink(RejectLinkCommand command);
    void activateLink(ActivateLinkCommand command);
    void disableLink(DisableLinkCommand command);
    void deleteLink(DeleteLinkCommand command);
}
