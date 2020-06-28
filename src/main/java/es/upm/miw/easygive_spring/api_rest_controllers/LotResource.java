package es.upm.miw.easygive_spring.api_rest_controllers;

import es.upm.miw.easygive_spring.business_controllers.LotController;
import es.upm.miw.easygive_spring.dtos.LotDto;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
@RestController
@RequestMapping(LotResource.LOTS)
public class LotResource {

    public static final String LOTS = "/lots";

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
}
