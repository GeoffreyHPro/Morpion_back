package morpion.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import morpion.model.User;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Integer> {
    Mono<User> findByEmail(String email);
}
