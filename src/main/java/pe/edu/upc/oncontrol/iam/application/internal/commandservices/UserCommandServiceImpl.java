package pe.edu.upc.oncontrol.iam.application.internal.commandservices;


import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.iam.application.internal.outboundedservices.hashing.HashingService;
import pe.edu.upc.oncontrol.iam.application.internal.outboundedservices.tokens.TokenService;
import pe.edu.upc.oncontrol.iam.domain.model.aggregates.User;
import pe.edu.upc.oncontrol.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.oncontrol.iam.domain.model.commands.SignUpCommand;
import pe.edu.upc.oncontrol.iam.domain.services.UserCommandService;
import pe.edu.upc.oncontrol.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;

    private UserCommandServiceImpl(UserRepository userRepository,
                                   HashingService hashingService,
                                   TokenService tokenService){
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        Optional<User> userOpt;

        if (command.identifier().contains("@")) {
            userOpt = userRepository.findByEmail(command.identifier());
        } else {
            userOpt = userRepository.findByUsername(command.identifier());
        }
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        User user = userOpt.get();

        if (!hashingService.matches(command.password(), user.getPassword())) {
            return Optional.empty();
        }
        String token = tokenService.generateToken(user.getUsername(), user.getRole().name(), user.getId());

        return Optional.of(ImmutablePair.of(user, token));
    }


    @Override
    public Optional<User> handle(SignUpCommand command){
        if(userRepository.existsByUsername(command.username()))
            throw new RuntimeException("Username already exists");
        if (command.email() != null && userRepository.existsByEmail(command.email())) {
            throw new RuntimeException("Email already exists");
        }
        var user = new User(
                command.username(),
                command.email(),
                hashingService.encode(command.password()),
                command.role());
        userRepository.save(user);
        return userRepository.findByUsername(command.username());
    }

}

