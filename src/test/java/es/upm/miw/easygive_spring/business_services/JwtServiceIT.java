package es.upm.miw.easygive_spring.business_services;

import es.upm.miw.easygive_spring.TestConfig;
import es.upm.miw.easygive_spring.exceptions.JwtException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestConfig
class JwtServiceIT {

    @Autowired
    private JwtService jwtService;

    @Test
    void testJwtExceptionNotBearer() {
        assertThrows(JwtException.class, () -> this.jwtService.user("Not Bearer"));
    }

    @Test
    void testJwtExceptionTokenError() {
        assertThrows(JwtException.class, () -> this.jwtService.user("Bearer error.error.error"));
    }

}
