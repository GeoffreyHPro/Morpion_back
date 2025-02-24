package morpion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import morpion.model.User;
import morpion.repository.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Mono<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public Mono<User> addUser(String email, String password) {
        return userRepository.save(new User(email, password));
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }
}
