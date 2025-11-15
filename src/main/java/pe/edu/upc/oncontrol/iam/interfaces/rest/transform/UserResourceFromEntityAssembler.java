package pe.edu.upc.oncontrol.iam.interfaces.rest.transform;

import pe.edu.upc.oncontrol.iam.domain.model.aggregates.User;
import pe.edu.upc.oncontrol.iam.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User user){
        return new UserResource(user.getId(), user.getUsername(), user.getRole());
    }
}
