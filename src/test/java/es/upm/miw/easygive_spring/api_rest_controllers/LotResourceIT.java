package es.upm.miw.easygive_spring.api_rest_controllers;

import es.upm.miw.easygive_spring.data_services.DatabaseSeederService;
import es.upm.miw.easygive_spring.dtos.LotDto;
import es.upm.miw.easygive_spring.repositories.LotRepository;
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

    @Autowired
    private LotRepository lotRepository;

    LotDto createLotDto (String image, String title, String description, String schedule, Boolean wish, Boolean food, Boolean delivered, String username) {
        LotDto lotDto = new LotDto();
        lotDto.setImage(image);
        lotDto.setTitle(title);
        lotDto.setDescription(description);
        lotDto.setSchedule(schedule);
        lotDto.setWish(wish);
        lotDto.setFood(food);
        lotDto.setDelivered(delivered);
        lotDto.setUsername(username);
        return lotDto;
    }

    LotDto seedDataLot() {
        return this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + LotResource.LOTS)
                .body(BodyInserters.fromObject(
                        this.createLotDto(this.logo, "t002", "d002", "s002", true, false, false, "u001")))
                .exchange()
                .expectStatus().isOk().expectBody(LotDto.class)
                .returnResult().getResponseBody();
    }

    @Test
    void testCreateLot() {
        LotDto lotDto = this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + LotResource.LOTS)
                .body(BodyInserters.fromObject(
                        this.createLotDto(this.logo, "t001", "d001", "s001", true, false, false, "u001")))
                .exchange()
                .expectStatus().isOk().expectBody(LotDto.class)
                .returnResult().getResponseBody();
        assertNotNull(lotDto);
        this.lotRepository.deleteById(lotDto.getId());
    }

    @Test
    void testCreateBadLot() {
        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + LotResource.LOTS)
                .body(BodyInserters.fromObject(
                        this.createLotDto(null, "t002", null, null, true, null, null, null)))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testCreateLotUserNotExist() {
        this.restService.loginAdmin(webTestClient)
                .post().uri(contextPath + LotResource.LOTS)
                .body(BodyInserters.fromObject(
                        this.createLotDto(this.logo, "t003", "d003", "s003", false, true, false, "1")))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testCreateLotWithoutLogin() {
        this.webTestClient
                .post().uri(contextPath + LotResource.LOTS)
                .body(BodyInserters.fromObject(
                        this.createLotDto(this.logo, "t001", "d001", "s001", true, false, false, "admin")))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testReadLot() {
        LotDto lotDto = this.seedDataLot();
        this.restService.loginAdmin(webTestClient)
                .get().uri(contextPath + LotResource.LOTS + LotResource.LOT_ID, lotDto.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(LotDto.class)
                .value(Assertions::assertNotNull)
                .value(lotRead -> assertEquals("t002", lotRead.getTitle()));
        this.lotRepository.deleteById(lotDto.getId());
    }

    @Test
    void testReadLotNotExist() {
        this.restService.loginAdmin(webTestClient)
                .get().uri(contextPath + LotResource.LOTS + LotResource.LOT_ID, "0")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testUpdateLot(){
        LotDto lotDto = this.seedDataLot();
        lotDto.setTitle("t003");
        this.restService.loginAdmin(webTestClient)
                .put().uri(contextPath + LotResource.LOTS + LotResource.LOT_ID, lotDto.getId())
                .body(BodyInserters.fromObject(lotDto))
                .exchange()
                .expectStatus().isOk()
                .expectBody(LotDto.class)
                .value(Assertions::assertNotNull)
                .value(lotUpdate -> assertEquals("t003", lotUpdate.getTitle()));
        this.lotRepository.deleteById(lotDto.getId());
    }

    @Test
    void testUpdateLotNotExist(){
        this.restService.loginAdmin(webTestClient)
                .put().uri(contextPath + LotResource.LOTS + LotResource.LOT_ID, "0")
                .body(BodyInserters.fromObject(
                        this.createLotDto(this.logo, "t004", "d004", "s004", true, false, false, "u001")
                )).exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testUpdateLotUserNotExist(){
        LotDto lotDto = this.seedDataLot();
        lotDto.setUsername("0");
        this.restService.loginAdmin(webTestClient)
                .put().uri(contextPath + LotResource.LOTS + LotResource.LOT_ID, lotDto.getId())
                .body(BodyInserters.fromObject(lotDto))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
        this.databaseSeederService.deleteAllAndInitializeAndSeedDataBase();
    }

    @Test
    void testDeleteLot(){
        LotDto lotDto = this.seedDataLot();
        this.restService.loginAdmin(webTestClient)
                .delete().uri(contextPath + LotResource.LOTS + LotResource.LOT_ID, lotDto.getId())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testDeleteLotNotExist(){
        this.restService.loginAdmin(webTestClient)
                .delete().uri(contextPath + LotResource.LOTS + LotResource.LOT_ID, "0")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testSearchLotFoodNotDelivered() {
        List<LotDto> lots = this.restService.loginAdmin(webTestClient)
                .get().uri(uriBuilder -> uriBuilder
                        .path(contextPath + LotResource.LOTS + LotResource.SEARCH_FOOD_DELIVERED)
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
                        .path(contextPath + LotResource.LOTS + LotResource.SEARCH_FOOD_DELIVERED)
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
                        .path(contextPath + LotResource.LOTS + LotResource.SEARCH_FOOD_DELIVERED)
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
                        .path(contextPath + LotResource.LOTS + LotResource.SEARCH_FOOD_DELIVERED)
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
                        .path(contextPath + LotResource.LOTS + LotResource.SEARCH_FOOD_DELIVERED)
                        .queryParam("food", "")
                        .queryParam("delivered","")
                        .build())
                .exchange().expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testSearchLotUser() {
        List<LotDto> lots = this.restService.loginAdmin(webTestClient)
                .get().uri(uriBuilder -> uriBuilder
                        .path(contextPath + LotResource.LOTS + LotResource.SEARCH_USER)
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
                        .path(contextPath + LotResource.LOTS + LotResource.SEARCH_USER)
                        .queryParam("username", "")
                        .build())
                .exchange().expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }
}