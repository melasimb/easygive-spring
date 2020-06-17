package es.upm.miw.easygive_spring.api_rest_controllers;

import es.upm.miw.easygive_spring.business_services.JwtService;
import es.upm.miw.easygive_spring.documents.Role;
import es.upm.miw.easygive_spring.dtos.TokenOutputDto;
import es.upm.miw.easygive_spring.exceptions.JwtException;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@Service
public class RestService {

    @Autowired
    private JwtService jwtService;

    @Value("${server.servlet.contextPath}")
    private String contextPath;

    @Value("${easygive.admin.username}")
    private String adminUsername;

    @Value("${easygive.admin.password}")
    private String adminPassword;

    private TokenOutputDto tokenDto;

    private boolean isRole(Role role) {
        try {
            return this.tokenDto != null && this.jwtService.roles("Bearer "
                    + tokenDto.getToken()).contains(role.name());
        } catch (JwtException e) {
            LogManager.getLogger(this.getClass()).error("------- is role -----------");
        }
        return false;
    }

    private WebTestClient login(Role role, String username, String pass, WebTestClient webTestClient) {
        if (!this.isRole(role)) {
            return login(username, pass, webTestClient);
        } else {
            return webTestClient.mutate()
                    .defaultHeader("Authorization", "Bearer " + this.tokenDto.getToken()).build();
        }
    }

    public WebTestClient login(String username, String pass, WebTestClient webTestClient) {
        this.tokenDto = webTestClient
                .mutate().filter(basicAuthentication(username, pass)).build()
                .post().uri(contextPath + UserResource.USERS + UserResource.TOKEN)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TokenOutputDto.class)
                .returnResult().getResponseBody();
        return webTestClient.mutate()
                .defaultHeader("Authorization", "Bearer " + this.tokenDto.getToken()).build();
    }

    public WebTestClient loginAdmin(WebTestClient webTestClient) {
        return this.login(Role.ADMIN, this.adminUsername, this.adminPassword, webTestClient);
    }

    public WebTestClient loginUser(WebTestClient webTestClient) {
        return this.login(Role.USER, "u001", "p001", webTestClient);
    }

    public void reLoadTestDB(WebTestClient webTestClient) {
        this.deleteDB(webTestClient);
        this.tokenDto = null;
        this.loginAdmin(webTestClient)
                .post().uri(contextPath + AdminResource.ADMINS + AdminResource.DB)
                .exchange()
                .expectStatus().isOk();
    }

    public void deleteDB(WebTestClient webTestClient) {
        this.loginAdmin(webTestClient)
                .delete().uri(contextPath + AdminResource.ADMINS + AdminResource.DB)
                .exchange()
                .expectStatus().isOk();
    }

    public TokenOutputDto getTokenDto() {
        return tokenDto;
    }

    public String getAdminUsername() {
        return adminUsername;
    }
}
