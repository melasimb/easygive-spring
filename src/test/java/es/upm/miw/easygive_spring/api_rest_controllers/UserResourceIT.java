package es.upm.miw.easygive_spring.api_rest_controllers;

import es.upm.miw.easygive_spring.data_services.DatabaseSeederService;
import es.upm.miw.easygive_spring.documents.User;
import es.upm.miw.easygive_spring.dtos.UserCredentialDto;
import es.upm.miw.easygive_spring.dtos.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@ApiTestConfig
class UserResourceIT {

    @Autowired
    private RestService restService;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private DatabaseSeederService databaseSeederService;

    @Test
    void testLoginAdmin() {
        this.restService.loginAdmin(this.webTestClient);
        assertTrue(this.restService.getTokenDto().getToken().length() > 10);
    }

    @Test
    void testLoginUser() {
        this.restService.loginUser(this.webTestClient);
        assertTrue(this.restService.getTokenDto().getToken().length() > 10);
    }

    @Test
    void testLoginAdminPassNull() {
        webTestClient
                .mutate().filter(basicAuthentication(restService.getAdminUsername(), "kk")).build()
                .post().uri(contextPath + UserResource.USERS + UserResource.TOKEN)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testLoginNonUsername() {
        webTestClient
                .mutate().filter(basicAuthentication("1", "kk")).build()
                .post().uri(contextPath + UserResource.USERS + UserResource.TOKEN)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testCreateUser() {
        this.webTestClient
                .post().uri(contextPath + UserResource.USERS)
                .body(BodyInserters.fromObject(
                        new UserDto(User.builder().username("u005").password("p005").location("C/EasyGive, 5").mobile("666666665").email("u005@gmail.com").build())))
                .exchange()
                .expectStatus().isOk().expectBody(UserDto.class)
                .value(Assertions::assertNotNull);
    }

    @Test
    void testCreateUserAlreadyExists() {
        this.webTestClient
                .post().uri(contextPath + UserResource.USERS)
                .body(BodyInserters.fromObject(
                        new UserDto(User.builder().username(restService.getAdminUsername()).password("6").location("C/Madroño, 14").mobile("600000000").email("admin@gmail.com").build())))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void testCreateBadUser() {
        this.webTestClient
                .post().uri(contextPath + UserResource.USERS)
                .body(BodyInserters.fromObject(
                        new UserDto(User.builder().username("u006").email("u006@gmail.com").build())))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testReadUser() {
        this.restService.loginAdmin(webTestClient)
                .get().uri(contextPath + UserResource.USERS + UserResource.USER_USERNAME, this.restService.getAdminUsername())
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(Assertions::assertNotNull)
                .value(userRead -> assertEquals(this.restService.getAdminUsername(), userRead.getUsername()));
    }

    @Test
    void testReadUserNotExist() {
        this.restService.loginAdmin(webTestClient)
                .get().uri(contextPath + UserResource.USERS + UserResource.USER_USERNAME, "0")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testReadUserWithoutLogin() {
        this.webTestClient
                .get().uri(contextPath + UserResource.USERS + UserResource.USER_USERNAME, this.restService.getAdminUsername())
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testUpdateUser() {
        this.restService.loginAdmin(webTestClient)
                .put().uri(contextPath + UserResource.USERS + UserResource.USER_USERNAME, this.restService.getAdminUsername())
                .body(BodyInserters.fromObject(
                        new UserDto(User.builder().username("admin").password("6").location("C/Madroño, 14").mobile("600000000").email("update@gmail.com").build())
                )).exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(Assertions::assertNotNull)
                .value(userUpdate -> assertEquals("update@gmail.com", userUpdate.getEmail()));
        this.databaseSeederService.deleteAllAndInitializeAndSeedDataBase();
    }

    @Test
    void testUpdateUserNotExist() {
        this.restService.loginAdmin(webTestClient)
                .put().uri(contextPath + UserResource.USERS + UserResource.USER_USERNAME, "0")
                .body(BodyInserters.fromObject(
                        new UserDto(User.builder().username("admin").password("6").location("C/Madroño, 14").mobile("600000000").email("update@gmail.com").build())
                )).exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testUpdatePasswordUser() {
        this.restService.loginAdmin(webTestClient)
                .patch().uri(contextPath + UserResource.USERS + UserResource.PASSWORD + UserResource.USER_USERNAME, this.restService.getAdminUsername())
                .body(BodyInserters.fromObject(
                        new UserCredentialDto("123456")
                )).exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .value(Assertions::assertNotNull)
                .value(userUpdate -> assertTrue(new BCryptPasswordEncoder().matches("123456", userUpdate.getPassword())));
        this.databaseSeederService.deleteAllAndInitializeAndSeedDataBase();
    }

    @Test
    void testUpdatePasswordUserNotExist() {
        this.restService.loginAdmin(webTestClient)
                .patch().uri(contextPath + UserResource.USERS + UserResource.PASSWORD + UserResource.USER_USERNAME, "0")
                .body(BodyInserters.fromObject(
                        new UserCredentialDto("123456")
                )).exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }
}
