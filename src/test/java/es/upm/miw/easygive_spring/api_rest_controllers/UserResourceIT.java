package es.upm.miw.easygive_spring.api_rest_controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.reactive.server.WebTestClient;

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
}
