package es.upm.miw.easygive_spring.documents;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LotTest {

    @Test
    void testLotBuilder() {
        LocalDateTime registrationDate = LocalDateTime.now();
        Role[] roles = {Role.ADMIN};
        User user = User.builder().username("user-1").registrationDate(registrationDate).password("password-1")
                .location("location-1").email("user-1@gmail.com").active(false).roles(roles).build();
        Lot lot = Lot.builder().title("title-1").description("description-1").schedule("schedule-1").wish(true).food(false)
                .delivered(false).user(user).build();
        assertEquals("title-1", lot.getTitle());
        assertEquals("description-1", lot.getDescription());
        assertEquals("schedule-1", lot.getSchedule());
        assertEquals(true, lot.getWish());
        assertEquals(false, lot.getFood());
        assertEquals(false, lot.getDelivered());
        assertEquals(user, lot.getUser());
    }
}