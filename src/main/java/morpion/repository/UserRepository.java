package morpion.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import morpion.model.User;

public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

}
