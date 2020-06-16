package es.upm.miw.easygive_spring.documents;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void testUserBuilder() {
        LocalDateTime registrationDate = LocalDateTime.now();
        Role[] roles = {Role.ADMIN};
        User user = User.builder().username("user-1").registrationDate(registrationDate).password("password-1")
                .location("location-1").email("user-1@gmail.com").active(false).roles(roles).build();
        assertEquals("user-1", user.getUsername());
        assertEquals(registrationDate, user.getRegistrationDate());
        assertEquals("location-1", user.getLocation());
        assertEquals("user-1@gmail.com", user.getEmail());
        assertEquals(false, user.isActive());
        assertEquals(roles, user.getRoles());
    }
}