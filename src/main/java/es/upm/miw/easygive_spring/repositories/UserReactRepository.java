package es.upm.miw.easygive_spring.repositories;

import es.upm.miw.easygive_spring.documents.User;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;

public interface UserReactRepository extends ReactiveSortingRepository<User, String> {

    Mono<User> findByUsername(String username);
}