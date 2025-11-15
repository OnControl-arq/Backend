package pe.edu.upc.oncontrol.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(
        Long id,
        String username,
        String token,
        String role
) {
}