package meetup.user_service.user.service;

import meetup.user_service.exception.NotFoundException;
import meetup.user_service.exception.ValidationException;
import meetup.user_service.user.dao.UserRepository;
import meetup.user_service.user.dto.NewUserRequest;
import meetup.user_service.user.dto.UpdateUserRequest;
import meetup.user_service.user.dto.UserDto;
import meetup.user_service.user.mapper.UserMapper;
import meetup.user_service.user.model.User;
import meetup.user_service.user.util.PasswordUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordUtils passwordUtils;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, userMapper, passwordUtils);
    }

    @Test
    void createUser_Success() {
        NewUserRequest request = new NewUserRequest(
                "John",
                "john@example.com",
                "StrongP@ss1",
                "Hello");
        User user = User.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .password("hashedPassword")
                .aboutMe("Hello")
                .build();
        when(passwordUtils.isPasswordStrong(request.password())).thenReturn(true);
        when(passwordUtils.hashPassword("StrongP@ss1")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(new UserDto(1L, "John", "john@example.com", null, "Hello"));

        UserDto result = userService.createUser(1L, request);

        assertNotNull(result);
        assertEquals("John", result.name());
        assertEquals("john@example.com", result.email());
    }

    @Test
    void createUser_WeakPassword_ThrowsException() {
        NewUserRequest request = new NewUserRequest(
                "John",
                "john@example.com",
                "weak",
                "Hello");
        when(passwordUtils.isPasswordStrong(request.password())).thenReturn(false);
        when(passwordUtils.getNotStrongPasswordText()).thenReturn(
                "Password is not strong! Password: at least 8 characters, one uppercase, one lowercase, one digit, and one special character !@#$%^&*()-+"
        );

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(1L, request)
        );

        assertEquals(
                "Password is not strong! Password: at least 8 characters, one uppercase, one lowercase, one digit, and one special character !@#$%^&*()-+",
                exception.getMessage()
        );
    }

    @Test
    void updateUser_Success() {
        UpdateUserRequest request = new UpdateUserRequest(
                "John Updated",
                "NewP@ss1",
                "Updated bio");
        User user = User.builder()
                .id(1L).name("John")
                .email("john@example.com")
                .password("hashedPassword")
                .aboutMe("Hello")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordUtils.isPasswordStrong(request.password())).thenReturn(true);
        when(passwordUtils.verifyPassword("OldPassword", "hashedPassword")).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDto newUserDto = new UserDto(
                1L,
                "John Updated",
                "john@example.com",
                "NewP@ss1",
                "Updated bio");
        when(userMapper.toUserDto(user)).thenReturn(newUserDto);

        UserDto result = userService.updateUser(1L, "OldPassword", request);

        assertNotNull(result);
        assertEquals("John Updated", result.name());
        assertEquals("Updated bio", result.aboutMe());
    }

    @Test
    void getUser_Success() {
        User user = User.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .password("hashedPassword")
                .aboutMe("Hello")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserDto newUserDto = new UserDto(
                1L,
                "John",
                "john@example.com",
                "hashedPassword",
                "Hello");
        when(userMapper.toUserDtoWithPassword(user)).thenReturn(newUserDto);

        UserDto result = userService.getUser(1L, 1L);

        assertNotNull(result);
        assertEquals("John", result.name());
        assertEquals("Hello", result.aboutMe());
    }

    @Test
    void getUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class, () -> userService.getUser(1L, 1L)
        );

        assertEquals("User id = 1 not found!", exception.getMessage());
    }

    @Test
    void getUsers_Success() {
        List<User> users = List.of(
                User.builder().id(1L).name("User1").build(),
                User.builder().id(2L).name("User2").build()
        );
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(users));
        when(userMapper.toUserDtoList(users)).thenReturn(List.of(
                new UserDto(1L, "User1", "user1@example.com", null, null),
                new UserDto(2L, "User2", "user2@example.com", null, null)
        ));

        List<UserDto> result = userService.getUsers(1L, pageable);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void deleteUser_Success() {
        User user = User.builder()
                .id(1L)
                .name("John")
                .password("hashedPassword")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordUtils.verifyPassword("OldPassword", "hashedPassword")).thenReturn(true);

        userService.deleteUser(1L, "OldPassword");

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class, () -> userService.deleteUser(1L, "password")
        );

        assertEquals("User id = 1 not found!", exception.getMessage());
    }

    @Test
    void verifyPassword_Strength_Success() {
        String password = "ValidP@ss1";
        when(passwordUtils.isPasswordStrong(password)).thenReturn(true);

        boolean result = passwordUtils.isPasswordStrong(password);

        assertTrue(result);
    }

    @Test
    void verifyPassword_Strength_Failure() {
        String password = "weak";
        when(passwordUtils.isPasswordStrong(password)).thenReturn(false);

        boolean result = passwordUtils.isPasswordStrong(password);

        assertFalse(result);
    }
}
