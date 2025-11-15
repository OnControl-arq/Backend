package pe.edu.upc.oncontrol.iam.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.iam.domain.model.aggregates.User;
import pe.edu.upc.oncontrol.iam.domain.model.queries.GetAllUsersQuery;
import pe.edu.upc.oncontrol.iam.domain.model.queries.GetUserByEmailQuery;
import pe.edu.upc.oncontrol.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.oncontrol.iam.domain.model.queries.GetUserByUsernameQuery;
import pe.edu.upc.oncontrol.iam.domain.services.UserQueryService;
import pe.edu.upc.oncontrol.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserQueryServicesImpl implements UserQueryService {
    private final UserRepository userRepository;
    public UserQueryServicesImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public List<User> handle(GetAllUsersQuery query) {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> handle(GetUserByEmailQuery query) {
        return userRepository.findByEmail(query.email());
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    @Override
    public Optional<User> handle(GetUserByUsernameQuery query) {
        return userRepository.findByUsername(query.username());
    }
}
