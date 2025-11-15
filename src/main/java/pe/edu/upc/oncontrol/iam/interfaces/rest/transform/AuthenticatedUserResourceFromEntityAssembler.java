package pe.edu.upc.oncontrol.iam.interfaces.rest.transform;


import pe.edu.upc.oncontrol.iam.domain.model.aggregates.User;
import pe.edu.upc.oncontrol.iam.interfaces.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, String token){
        return new AuthenticatedUserResource(user.getId(), user.getUsername(), token, user.getRole().name());
    }
}
