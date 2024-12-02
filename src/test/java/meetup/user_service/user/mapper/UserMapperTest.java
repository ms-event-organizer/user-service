package meetup.user_service.user.mapper;

import meetup.user_service.user.dto.UpdateUserRequest;
import meetup.user_service.user.dto.UserDto;
import meetup.user_service.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserMapperTest {
    @Autowired
    private JacksonTester<User> userJacksonTester;

    @Autowired
    private JacksonTester<UserDto> userDtoJacksonTester;

    @Test
    void toUser() throws IOException {
        User user = User.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .password("hashedPassword")
                .aboutMe("Hello")
                .build();

        JsonContent<User> result = userJacksonTester.write(user);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john@example.com");
        assertThat(result).extractingJsonPathStringValue("$.password").isEqualTo("hashedPassword");
    }


    @Test
    void toUserDto() throws IOException {
        UserDto userDto = new UserDto(
                1L,
                "John",
                "john@example.com",
                "NewP@ss1",
                "Bio");

        JsonContent<UserDto> result = userDtoJacksonTester.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john@example.com");
        assertThat(result).extractingJsonPathStringValue("$.password").isEqualTo("NewP@ss1");
    }

    @Test
    void toUserDtoWithPassword() throws IOException {
        UserDto userDto = new UserDto(
                1L,
                "John",
                "john@example.com",
                "NewP@ss1",
                "Bio");

        JsonContent<UserDto> result = userDtoJacksonTester.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john@example.com");
        assertThat(result).extractingJsonPathStringValue("$.password").isEqualTo("NewP@ss1");
    }

    @Test
    void updateUser() throws IOException {
        User user = User.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .password("hashedPassword")
                .aboutMe("Hello")
                .build();

        UpdateUserRequest request = new UpdateUserRequest(
                "John Updated",
                "NewP@ss1",
                "Updated bio");

        UserDto userDto = new UserDto(
                1L,
                "John Updated",
                "john@example.com",
                "NewP@ss1",
                "Updated bio");

        JsonContent<UserDto> result = userDtoJacksonTester.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John Updated");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john@example.com");
        assertThat(result).extractingJsonPathStringValue("$.password").isEqualTo("NewP@ss1");

    }
}