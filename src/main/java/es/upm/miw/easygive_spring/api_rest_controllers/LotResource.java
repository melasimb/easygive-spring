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
    public static final String SEARCH_FOOD_DELIVERED = "/searchFoodDelivered";
    public static final String SEARCH_USER = "/searchUser";
    public static final String LOT_ID = "/{id}";

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

    @GetMapping(value = LOT_ID)
    public Mono<LotDto> read(@PathVariable String id) {
        return this.lotController.read(id)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @PutMapping(value = LOT_ID)
    public Mono<LotDto> update(@PathVariable String id, @Valid @RequestBody LotDto lotDto) {
        return this.lotController.update(id, lotDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @DeleteMapping(value = LOT_ID)
    public Mono<Void> delete(@PathVariable String id) {
        return this.lotController.delete(id)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @GetMapping(value = SEARCH_FOOD_DELIVERED)
    public Flux<LotDto> searchByFoodAndDelivered(@RequestParam Boolean food, @RequestParam Boolean delivered) {
        return this.lotController.searchByFoodAndDelivered(new LotSearchDto(food, delivered))
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @GetMapping(value = SEARCH_USER)
    public Flux<LotDto> searchByUser(@RequestParam String username) {
        return this.lotController.searchByUser(username)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }
}
