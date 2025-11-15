package pe.edu.upc.oncontrol.iam.domain.model.commands;

public record SignInCommand(
        String identifier,
        String password
) {
}
