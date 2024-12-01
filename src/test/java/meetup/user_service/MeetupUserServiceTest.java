package meetup.user_service;

import meetup.user_service.user.controller.UserController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MeetupUserServiceTest {

  @Autowired
    private UserController userController;

    @Test
    public void contextLoads() {
    Assertions.assertThat(userController).isNotNull();
    }
}