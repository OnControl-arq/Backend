package pe.edu.upc.oncontrol.iam.interfaces.acl;


import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.iam.domain.model.aggregates.User;
import pe.edu.upc.oncontrol.iam.domain.model.commands.SignUpCommand;
import pe.edu.upc.oncontrol.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.oncontrol.iam.domain.model.queries.GetUserByUsernameQuery;
import pe.edu.upc.oncontrol.iam.domain.model.valueobjects.Roles;
import pe.edu.upc.oncontrol.iam.domain.services.UserCommandService;
import pe.edu.upc.oncontrol.iam.domain.services.UserQueryService;
import pe.edu.upc.oncontrol.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Component
public class IamContextFacade {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public IamContextFacade(UserCommandService userCommandService,
                            UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    public Long createUser(String username, String email ,String password, Roles role) {
        var signUpCommand = new SignUpCommand(username, email, password, role);
        var result = userCommandService.handle(signUpCommand);
        return result.map(AuditableAbstractAggregateRoot::getId).orElse(0L);
    }

    public Long fetchUserIdByUsername(String username) {
        var query = new GetUserByUsernameQuery(username);
        return userQueryService.handle(query).map(AuditableAbstractAggregateRoot::getId).orElse(0L);
    }

    public String fetchUsernameByUserId(Long userId) {
        var query = new GetUserByIdQuery(userId);
        return userQueryService.handle(query).map(User::getUsername).orElse(Strings.EMPTY);
    }

    public boolean isValidUserWithRole(Long userId, Roles expectedRole){
        var query = new GetUserByIdQuery(userId);
        return userQueryService.handle(query)
                .filter(user -> user.getRole() == expectedRole && user.isActive())
                .isPresent();
    }

}