package morpion.configuration.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import morpion.exception.NotFoundException;
import morpion.service.JWTService;
import morpion.service.UserService;
import reactor.core.publisher.Mono;

@Component
public class AuthManager implements ReactiveAuthenticationManager {

    final JWTService jwtService;
    final UserService userService;

    public AuthManager(JWTService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .cast(BearerToken.class)
                .flatMap(auth -> {
                    String getUserName = jwtService.getUserName(auth.getCredentials());

                    return userService.getUserByEmail(getUserName)

                            .flatMap(user -> {
                                if (user.getUsername() == null) {
                                    return Mono.error(new IllegalArgumentException("User not found"));
                                }

                                if (jwtService.validate(user, auth.getCredentials())) {
                                    return Mono.just(new UsernamePasswordAuthenticationToken(
                                            user.getUsername(),
                                            user.getPassword(),
                                            user.getAuthorities()));
                                }

                                return Mono.error(new IllegalArgumentException("Invalid/Expired Token"));
                            })
                            .onErrorResume(NotFoundException.class,
                                    err -> Mono.error(new NotFoundException()));
                });
    }
}
