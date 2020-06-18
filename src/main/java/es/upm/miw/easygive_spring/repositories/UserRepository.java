package es.upm.miw.easygive_spring.repositories;

import es.upm.miw.easygive_spring.documents.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String username);

}
