package pe.edu.upc.oncontrol.iam.domain.model.commands;
import pe.edu.upc.oncontrol.iam.domain.model.valueobjects.Roles;

public record SignUpCommand(
   String username,
   String email,
   String password,
   Roles role
) {}
