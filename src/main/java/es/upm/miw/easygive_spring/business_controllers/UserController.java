package es.upm.miw.easygive_spring.business_controllers;

import es.upm.miw.easygive_spring.business_services.JwtService;
import es.upm.miw.easygive_spring.documents.Role;
import es.upm.miw.easygive_spring.documents.User;
import es.upm.miw.easygive_spring.dtos.TokenOutputDto;
import es.upm.miw.easygive_spring.dtos.UserDto;
import es.upm.miw.easygive_spring.exceptions.ConflictException;
import es.upm.miw.easygive_spring.repositories.UserReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Controller
public class UserController {

    private UserReactRepository userReactRepository;
    private JwtService jwtService;

    @Autowired
    public UserController(UserReactRepository userReactRepository, JwtService jwtService) {
        this.userReactRepository = userReactRepository;
        this.jwtService = jwtService;
    }

    public Mono<TokenOutputDto> login(String username) {
        return this.userReactRepository.findByUsername(username).map(
                user -> {
                    String[] roles = Arrays.stream(user.getRoles()).map(Role::name).toArray(String[]::new);
                    return new TokenOutputDto(jwtService.createToken(user.getUsername(), roles));
                }
        );
    }

    private Mono<Void> noExistByUsername(String username) {
        return this.userReactRepository.findByUsername(username)
                .handle((document, sink) -> sink.error(new ConflictException("This username already exists")));
    }

    public Mono<UserDto> create(UserDto userDto) {
        Mono<Void> noExistByUsername = this.noExistByUsername(userDto.getUsername());
        User user = User.builder().username(userDto.getUsername()).password(userDto.getPassword()).location(userDto.getLocation()).mobile(userDto.getMobile()).email(userDto.getEmail()).build();
        return Mono.when(noExistByUsername).then(this.userReactRepository.save(user)).map(UserDto::new);
    }
}