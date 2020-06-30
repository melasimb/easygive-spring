package es.upm.miw.easygive_spring.business_controllers;

import es.upm.miw.easygive_spring.documents.Lot;
import es.upm.miw.easygive_spring.documents.User;
import es.upm.miw.easygive_spring.dtos.LotDto;
import es.upm.miw.easygive_spring.dtos.LotSearchDto;
import es.upm.miw.easygive_spring.exceptions.NotFoundException;
import es.upm.miw.easygive_spring.repositories.LotReactRepository;
import es.upm.miw.easygive_spring.repositories.UserReactRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Base64;

@Controller
public class LotController {

    private LotReactRepository lotReactRepository;

    private UserReactRepository userReactRepository;

    @Autowired
    public LotController(LotReactRepository lotReactRepository, UserReactRepository userReactRepository) {
        this.lotReactRepository = lotReactRepository;
        this.userReactRepository = userReactRepository;
    }

    public Mono<LotDto> create(LotDto lotDto) {
        Binary binaryImage = new Binary(BsonBinarySubType.BINARY, Base64.getDecoder().decode(lotDto.getImage()));
        Lot lot = Lot.builder().image(binaryImage).title(lotDto.getTitle())
                .description(lotDto.getDescription()).schedule(lotDto.getSchedule()).wish(lotDto.getWish()).food(lotDto.getFood())
                .delivered(lotDto.getDelivered()).build();
        Mono<Void> user = this.userReactRepository.findByUsername(lotDto.getUsername())
                .switchIfEmpty(Mono.error(new NotFoundException("User (" + lotDto.getUsername() + ")")))
                .doOnNext(lot::setUser).then();
        return Mono.when(user).then(this.lotReactRepository.save(lot)).map(LotDto::new);
    }

    public Flux<LotDto> searchByFoodAndDelivered(LotSearchDto lotSearchDto) {
        return this.lotReactRepository.findByFoodAndDelivered(lotSearchDto.getFood(), lotSearchDto.getDelivered())
                .switchIfEmpty(Flux.error(new NotFoundException("There aren't lots")))
                .map(LotDto::new);
    }

    public Flux<LotDto> searchByUser(String username) {
        Mono<User> user = this.userReactRepository.findByUsername(username)
                .switchIfEmpty(Mono.error(new NotFoundException("User (" + username + ")")));
        return this.lotReactRepository.findByUser(user)
                .switchIfEmpty(Flux.error(new NotFoundException("There aren't lots")))
                .map(LotDto::new);
    }
}
