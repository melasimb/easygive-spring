package es.upm.miw.easygive_spring.api_rest_controllers;

import es.upm.miw.easygive_spring.data_services.DatabaseSeederService;
import es.upm.miw.easygive_spring.dtos.LotDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

    @Autowired
    private DatabaseSeederService databaseSeederService;

    @Test
    void testCreateLot() {
        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + LotResource.LOTS)
                .body(BodyInserters.fromObject(
                        new LotDto(this.logo, "t001", "d001", "s001", true, false, false, "u001", null)))
                .exchange()
                .expectStatus().isOk().expectBody(LotDto.class)
                .value(Assertions::assertNotNull);
        this.databaseSeederService.deleteAllAndInitializeAndSeedDataBase();
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

    @Test
    void testSearchLotFoodNotDelivered() {
        List<LotDto> lots = this.restService.loginAdmin(webTestClient)
                .get().uri(uriBuilder -> uriBuilder
                        .path(contextPath + LotResource.LOTS + LotResource.FOOD_DELIVERED_SEARCH)
                        .queryParam("food", true)
                        .queryParam("delivered",false)
                        .build())
                .exchange().expectStatus().isOk().expectBodyList(LotDto.class)
                .returnResult().getResponseBody();
        assertNotNull(lots);
        assertEquals(2, lots.size());
    }

    @Test
    void testSearchLotFoodDelivered() {
        List<LotDto> lots = this.restService.loginAdmin(webTestClient)
                .get().uri(uriBuilder -> uriBuilder
                        .path(contextPath + LotResource.LOTS + LotResource.FOOD_DELIVERED_SEARCH)
                        .queryParam("food", true)
                        .queryParam("delivered",true)
                        .build())
                .exchange().expectStatus().isOk().expectBodyList(LotDto.class)
                .returnResult().getResponseBody();
        assertNotNull(lots);
        assertEquals(1, lots.size());
    }


    @Test
    void testSearchLotNonFoodNotDelivered() {
        List<LotDto> lots = this.restService.loginAdmin(webTestClient)
                .get().uri(uriBuilder -> uriBuilder
                        .path(contextPath + LotResource.LOTS + LotResource.FOOD_DELIVERED_SEARCH)
                        .queryParam("food", false)
                        .queryParam("delivered",false)
                        .build())
                .exchange().expectStatus().isOk().expectBodyList(LotDto.class)
                .returnResult().getResponseBody();
        assertNotNull(lots);
        assertEquals(1, lots.size());
    }

    @Test
    void testSearchLotNonFoodDelivered() {
        List<LotDto> lots = this.restService.loginAdmin(webTestClient)
                .get().uri(uriBuilder -> uriBuilder
                        .path(contextPath + LotResource.LOTS + LotResource.FOOD_DELIVERED_SEARCH)
                        .queryParam("food", false)
                        .queryParam("delivered",true)
                        .build())
                .exchange().expectStatus().isOk().expectBodyList(LotDto.class)
                .returnResult().getResponseBody();
        assertNotNull(lots);
        assertEquals(1, lots.size());
    }

    @Test
    void testSearchLotFoodDeliveredNotFound() {
        this.restService.loginAdmin(webTestClient)
                .get().uri(uriBuilder -> uriBuilder
                        .path(contextPath + LotResource.LOTS + LotResource.FOOD_DELIVERED_SEARCH)
                        .queryParam("food", "")
                        .queryParam("delivered","")
                        .build())
                .exchange().expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testSearchLotUser() {
        List<LotDto> lots = this.restService.loginAdmin(webTestClient)
                .get().uri(uriBuilder -> uriBuilder
                        .path(contextPath + LotResource.LOTS + LotResource.USER_SEARCH)
                        .queryParam("username", "admin")
                        .build())
                .exchange().expectStatus().isOk().expectBodyList(LotDto.class)
                .returnResult().getResponseBody();
        assertNotNull(lots);
        assertEquals(5, lots.size());
    }

    @Test
    void testSearchLotUserNotFound() {
        this.restService.loginAdmin(webTestClient)
                .get().uri(uriBuilder -> uriBuilder
                        .path(contextPath + LotResource.LOTS + LotResource.USER_SEARCH)
                        .queryParam("username", "")
                        .build())
                .exchange().expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }
}