package lab.lab5.service;

import lab.lab5.dto.RegistrationRequest;
import lab.lab5.entity.User;
import lab.lab5.entity.RolesHasUsers;
import lab.lab5.repository.UserRepository;
import lab.lab5.repository.RoleRepository;
import lab.lab5.repository.RolesHasUsersRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RolesHasUsersRepository rolesHasUsersRepository;

    public Mono<User> register(RegistrationRequest request) {

        if (request.username() == null ||
                request.username().isBlank() ||
                request.password() == null ||
                request.password().isBlank()) {

            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username and password are required"
            ));
        }

        String username = request.username().trim();

        return userRepository.findByUsername(username)
                .flatMap(user -> Mono.<User>error(
                        new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "User already exists"
                        )
                ))
                .switchIfEmpty(
                        userRepository.save(
                                new User(
                                        null,
                                        username,
                                        request.password()
                                )
                        ).flatMap(user ->
                                roleRepository.findAll()
                                        .filter(role ->
                                                role.getName().equals("ROLE_User"))
                                        .next()
                                        .switchIfEmpty(Mono.error(
                                                new ResponseStatusException(
                                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                                        "User role not found"
                                                )
                                        ))
                                        .flatMap(role ->
                                                rolesHasUsersRepository.save(
                                                        new RolesHasUsers(
                                                                null,
                                                                role.getId(),
                                                                user.getId()
                                                        )
                                                ).thenReturn(user)
                                        )
                        )
                );
    }
}
