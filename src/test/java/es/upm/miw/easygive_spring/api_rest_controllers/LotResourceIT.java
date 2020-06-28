package es.upm.miw.easygive_spring.api_rest_controllers;

import es.upm.miw.easygive_spring.dtos.LotDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ApiTestConfig
class LotResourceIT {

    @Autowired
    private RestService restService;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${easygive.logo}")
    private String logo;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Test
    void testCreateLot() {
        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + LotResource.LOTS)
                .body(BodyInserters.fromObject(
                        new LotDto(this.logo, "t001", "d001", "s001", true, false, false, "admin", null)))
                .exchange()
                .expectStatus().isOk().expectBody(LotDto.class)
                .value(Assertions::assertNotNull);
    }

    @Test
    void testCreateBadLot() {
        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + LotResource.LOTS)
                .body(BodyInserters.fromObject(
                        new LotDto(null, "t002", null, null, true, null, null, null, null)))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testCreateLotUserNotExists() {
        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + LotResource.LOTS)
                .body(BodyInserters.fromObject(
                        new LotDto(this.logo, "t003", "d003", "s003", false, true, false, "1", null)))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testCreateLotWithoutLogin() {
        this.webTestClient
                .post().uri(contextPath + LotResource.LOTS)
                .body(BodyInserters.fromObject(
                        new LotDto(this.logo, "t001", "d001", "s001", true, false, false, "admin", null)))
                .exchange()
                .expectStatus().isUnauthorized();
    }
}