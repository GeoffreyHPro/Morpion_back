package morpion.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import morpion.model.User;
import morpion.request.LoginRequest;
import morpion.response.Response;
import morpion.service.JWTService;
import morpion.service.UserService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    final JWTService JWTService;
    final PasswordEncoder passwordEncoder;
    final UserService userService;

    public AuthController(UserService userService, JWTService JWTService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.JWTService = JWTService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signIn")
    public Mono<ResponseEntity<Response<String>>> login(@RequestBody LoginRequest user) {

        Mono<User> foundUser = userService.getUserByEmail(user.getEmail());
        System.out.println(foundUser);

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
                            .body(new Response<String>("", "User not found. Please Register")));
        });
    }

    @PostMapping("/signUp")
    public Mono<User> addUser(@RequestBody LoginRequest user) {
        return userService.addUser(user.getEmail(), passwordEncoder.encode(user.getPassword()));
    }
}
