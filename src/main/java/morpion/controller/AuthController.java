package morpion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import morpion.model.User;
import morpion.request.LoginRequest;
import morpion.response.Response;
import morpion.service.JWTService;
import morpion.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    final ReactiveUserDetailsService users;
    final JWTService JWTService;
    final PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    public AuthController(ReactiveUserDetailsService users, JWTService JWTService, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.JWTService = JWTService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public Mono<ResponseEntity<Response<String>>> login(@RequestBody LoginRequest user) {

        Mono<UserDetails> foundUser = users.findByUsername(user.getEmail());

        return foundUser.flatMap(u -> {

            if (u != null) {
                if (passwordEncoder.matches(user.getPassword(), u.getPassword())) {
                    return Mono.just(ResponseEntity.ok(
                            new Response<String>(JWTService.generate(user.getEmail()), "success")));
                }
                return Mono.just(
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new Response<String>("", "Invalid Credentials")));
            }
            return Mono.just(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new Response<String>("", "User not found. Please Register"))

            );
        });
    }

    @PostMapping("/add")
    public Mono<User> addUser(@RequestBody LoginRequest user) {
        return userService.addUser(user.getEmail(), passwordEncoder.encode(user.getPassword()));
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/security")
    public Mono<ResponseEntity<Response<String>>> getResponse() {

        // String userName = authentication.getUsername();

        /*
         * Mono<UserDetails> foundUser =
         * users.findByUsername(authentication.getUsername()).defaultIfEmpty(null);
         * String token = authHeader.substring(7);
         * foundUser.map(u -> {
         * if (JWTService.validate(u, token)) {
         * return Mono.just(ResponseEntity.ok(
         * new Response<>("Welcome", "")));
         * }
         * return null;
         * });
         * return null;
         */

        return Mono.empty();
    }

    @GetMapping("/users")
    public Flux<User> getUsers() {
        return userService.getAllUsers();
    }

}
