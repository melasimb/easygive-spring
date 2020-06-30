package es.upm.miw.easygive_spring.api_rest_controllers;

import es.upm.miw.easygive_spring.business_controllers.LotController;
import es.upm.miw.easygive_spring.dtos.LotDto;
import es.upm.miw.easygive_spring.dtos.LotSearchDto;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
@RestController
@RequestMapping(LotResource.LOTS)
public class LotResource {

    public static final String LOTS = "/lots";
    public static final String FOOD_DELIVERED_SEARCH = "/foodDeliveredSearch";
    public static final String USER_SEARCH = "/userSearch";

    private LotController lotController;

    @Autowired
    public LotResource(LotController lotController) {
        this.lotController = lotController;
    }

    @PostMapping
    public Mono<LotDto> create(@Valid @RequestBody LotDto lotDto) {
        return this.lotController.create(lotDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @GetMapping(value = FOOD_DELIVERED_SEARCH)
    public Flux<LotDto> searchByFoodAndDelivered(@RequestParam Boolean food, @RequestParam Boolean delivered) {
        return this.lotController.searchByFoodAndDelivered(new LotSearchDto(food, delivered))
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @GetMapping(value = USER_SEARCH)
    public Flux<LotDto> searchByUser(@RequestParam String username) {
        return this.lotController.searchByUser(username)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }
}
