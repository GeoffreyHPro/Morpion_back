package morpion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import morpion.model.User;
import morpion.response.Response;
import morpion.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public Mono<ResponseEntity<Response<String>>> getResponse() {
        return Mono.empty();
    }

    @GetMapping("/users")
    public Flux<User> getUsers() {
        return userService.getAllUsers();
    }
}
