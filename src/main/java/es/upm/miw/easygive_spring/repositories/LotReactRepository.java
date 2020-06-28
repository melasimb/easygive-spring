package es.upm.miw.easygive_spring.repositories;

import es.upm.miw.easygive_spring.documents.Lot;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface LotReactRepository extends ReactiveSortingRepository<Lot, String> {
}
