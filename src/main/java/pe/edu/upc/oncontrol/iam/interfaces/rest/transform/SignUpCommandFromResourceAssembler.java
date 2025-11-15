package pe.edu.upc.oncontrol.iam.interfaces.rest.transform;


import pe.edu.upc.oncontrol.iam.domain.model.commands.SignUpCommand;
import pe.edu.upc.oncontrol.iam.interfaces.rest.resources.SignUpResource;

public class SignUpCommandFromResourceAssembler {
    public static SignUpCommand toCommandFromResource(SignUpResource resource){
        return new SignUpCommand(
                resource.username(),
                resource.email(),
                resource.password(),
                resource.role()
        );
    }
}
