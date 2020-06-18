package es.upm.miw.easygive_spring.api_rest_controllers;

import es.upm.miw.easygive_spring.business_controllers.UserController;
import es.upm.miw.easygive_spring.dtos.TokenOutputDto;
import es.upm.miw.easygive_spring.dtos.UserDto;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(UserResource.USERS)
public class UserResource {

    public static final String USERS = "/users";
    public static final String TOKEN = "/token";

    private UserController userController;

    @Autowired
    public UserResource(UserController userController) {
        this.userController = userController;
    }

    @PreAuthorize("authenticated")
    @PostMapping(value = TOKEN)
    public Mono<TokenOutputDto> login(@AuthenticationPrincipal User activeUser) {
        return userController.login(activeUser.getUsername())
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @PostMapping
    public Mono<UserDto> create(@Valid @RequestBody UserDto userDto) {
        return this.userController.create(userDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }
}