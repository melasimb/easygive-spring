package es.upm.miw.easygive_spring.repositories;

import es.upm.miw.easygive_spring.documents.Lot;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LotRepository extends MongoRepository<Lot, String> {
}
