package meetup.user_service.user.mapper;

import meetup.user_service.user.dto.UserDto;
import meetup.user_service.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserMapperTest {
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void testToUserDtoList_NullList() {
        List<UserDto> result = userMapper.toUserDtoList(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testToUserDtoList_EmptyList() {
        List<User> users = List.of();
        List<UserDto> result = userMapper.toUserDtoList(users);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testToUserDtoList_SingleElement() {
        User user = new User(
                1L,
                "John",
                "john@example.com",
                "password",
                "John bio");
        List<User> users = List.of(user);
        List<UserDto> result = userMapper.toUserDtoList(users);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getId(), result.get(0).id());
        assertEquals(user.getName(), result.get(0).name());
        assertEquals(user.getEmail(), result.get(0).email());
        assertEquals(user.getAboutMe(), result.get(0).aboutMe());
    }

    @Test
    void testToUserDto_NullUser() {
        UserDto result = userMapper.toUserDto(null);
        assertNull(result);
    }

    @Test
    void testToUserDto_ValidUser() {
        User user = new User(
                1L,
                "John",
                "john@example.com",
                "password",
                "John bio");
        UserDto result = userMapper.toUserDto(user);
        assertNotNull(result);
        assertEquals(user.getId(), result.id());
        assertEquals(user.getName(), result.name());
        assertEquals(user.getEmail(), result.email());
        assertNull(result.password());
        assertEquals(user.getAboutMe(), result.aboutMe());
    }

    @Test
    void testToUserDtoWithPassword_NullUser() {
        UserDto result = userMapper.toUserDtoWithPassword(null);
        assertNull(result);
    }

    @Test
    void testToUserDtoWithPassword_ValidUser() {
        User user = new User(
                1L,
                "John",
                "john@example.com",
                "password",
                "Hello");
        UserDto result = userMapper.toUserDtoWithPassword(user);
        assertNotNull(result);
        assertEquals(user.getId(), result.id());
        assertEquals(user.getName(), result.name());
        assertEquals(user.getEmail(), result.email());
        assertEquals(user.getPassword(), result.password());
        assertEquals(user.getAboutMe(), result.aboutMe());
    }

    @Test
    void testToUserDtoList_UsersWithNullValues() {
        User user1 = new User(
                1L,
                null,
                null,
                null,
                null);
        User user2 = new User(
                2L,
                "Alice",
                null,
                null,
                "Alice bio");
        List<User> users = List.of(user1, user2);
        List<UserDto> result = userMapper.toUserDtoList(users);
        assertEquals(2, result.size());
        assertNull(result.get(0).name());
        assertNull(result.get(0).email());
        assertNull(result.get(0).password());
        assertNull(result.get(0).aboutMe());
        assertEquals("Alice", result.get(1).name());
        assertNull(result.get(1).email());
        assertNull(result.get(1).password());
        assertEquals("Alice bio", result.get(1).aboutMe());
    }
}
