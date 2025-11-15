package pe.edu.upc.oncontrol.profile.domain.model.commands.link;

import java.util.UUID;

public record DisableLinkCommand(UUID externalId, Long actorUserId) {
}
