package pe.edu.upc.oncontrol.profile.interfaces.rest.resources.link;

import pe.edu.upc.oncontrol.profile.domain.model.valueobjects.LinkStatus;

public record UpdateStatusLinkResource(
        LinkStatus status
) {
}
