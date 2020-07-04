package es.upm.miw.easygive_spring.api_rest_controllers;

import es.upm.miw.easygive_spring.business_controllers.UserController;
import es.upm.miw.easygive_spring.dtos.TokenOutputDto;
import es.upm.miw.easygive_spring.dtos.UserCredentialDto;
import es.upm.miw.easygive_spring.dtos.UserDto;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(UserResource.USERS)
public class UserResource {

    public static final String USERS = "/users";
    public static final String TOKEN = "/token";
    public static final String USER_USERNAME = "/{username}";
    public static final String PASSWORD = "/password";

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

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping(value = USER_USERNAME)
    public Mono<UserDto> read(@PathVariable String username) {
        return this.userController.read(username)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PutMapping(value = USER_USERNAME)
    public Mono<UserDto> update (@PathVariable String username, @Valid @RequestBody UserDto userDto) {
        return this.userController.update(username, userDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PatchMapping(value = PASSWORD + USER_USERNAME)
    public Mono<UserDto> updatePassword (@PathVariable String username, @Valid @RequestBody UserCredentialDto userCredentialDto) {
        return this.userController.updatePassword(username, userCredentialDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }
}