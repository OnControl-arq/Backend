package pe.edu.upc.oncontrol.iam.interfaces.rest.transform;

import pe.edu.upc.oncontrol.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.oncontrol.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource signInresource) {
        return new SignInCommand(
                signInresource.identifier(),
                signInresource.password()
        );
    }
}
