package es.upm.miw.easygive_spring.repositories;

import es.upm.miw.easygive_spring.documents.Lot;
import es.upm.miw.easygive_spring.documents.User;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LotReactRepository extends ReactiveSortingRepository<Lot, String> {
    Flux<Lot> findByFoodAndDelivered(Boolean food, Boolean delivered);
    Flux<Lot> findByUser(Mono<User> user);
}
