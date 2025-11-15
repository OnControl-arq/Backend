package pe.edu.upc.oncontrol.iam.interfaces.rest.resources;

import pe.edu.upc.oncontrol.iam.domain.model.valueobjects.Roles;

public record UserResource(
        Long id,
        String username,
        Roles role
        ) {
}
