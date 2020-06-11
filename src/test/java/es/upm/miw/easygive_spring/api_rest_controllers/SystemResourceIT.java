package es.upm.miw.easygive_spring.api_rest_controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.reactive.server.WebTestClient;

import static es.upm.miw.easygive_spring.api_rest_controllers.SystemResource.SYSTEM;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ApiTestConfig
class SystemResourceIT {

    @Autowired
    private WebTestClient webTestClient;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Test
    void testReadVersionBadge() {

        this.webTestClient
                .get().uri(contextPath + SYSTEM + SystemResource.VERSION_BADGE)
                .exchange()
                .expectStatus().isOk()
                .expectBody(byte[].class)
                .value(Assertions::assertNotNull)
                .value(svg -> assertTrue(new String(svg).startsWith("<svg")));
    }

    @Test
    void testReadVersion() {
        this.webTestClient
                .get().uri(contextPath + SYSTEM + SystemResource.APP_INFO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AppInfoDto.class)
                .value(Assertions::assertNotNull)
                .value(appInfo -> assertNotNull(appInfo.getVersion()));
    }

    @Test
    void testException() {
        this.webTestClient.post().uri(contextPath + SYSTEM + SystemResource.VERSION_BADGE)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
