package meetup.user_service.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meetup.user_service.exception.NotFoundException;
import meetup.user_service.exception.ValidationException;
import meetup.user_service.user.dao.UserRepository;
import meetup.user_service.user.dto.NewUserRequest;
import meetup.user_service.user.dto.UpdateUserRequest;
import meetup.user_service.user.dto.UserDto;
import meetup.user_service.user.mapper.UserMapper;
import meetup.user_service.user.model.User;
import meetup.user_service.user.util.PasswordUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordUtils passwordUtils;

    public UserDto createUser(Long userId, NewUserRequest newUserRequest) {
        checkPasswordStrength(newUserRequest.password());
        User newUser = userRepository.save(
                User.builder()
                        .name(newUserRequest.name())
                        .email(newUserRequest.email())
                        .password(passwordUtils.hashPassword(newUserRequest.password()))
                        .aboutMe(newUserRequest.aboutMe())
                        .build()
        );
        log.info("User with id = '{}' was created", newUser.getId());
        return userMapper.toUserDto(newUser);
    }

    public UserDto updateUser(Long userId, String userPassword, UpdateUserRequest updateUserRequest) {
        User user = getUserById(userId);
        verifyPassword(userPassword, user.getPassword());
        if (updateUserRequest.name() != null && !updateUserRequest.name().isBlank()) {
            user.setName(updateUserRequest.name());
        }
        if (updateUserRequest.aboutMe() != null && !updateUserRequest.aboutMe().isBlank()) {
            user.setAboutMe(updateUserRequest.aboutMe());
        }
        if (updateUserRequest.password() != null) {
            checkPasswordStrength(updateUserRequest.password());
            user.setPassword(updateUserRequest.password());
        }
        userRepository.save(user);
        log.info("User with id = '{}' was updated", user.getId());
        return userMapper.toUserDto(user);
    }

    public UserDto getUser(Long userId, Long id) {
        User user = getUserById(userId);
        UserDto userDto = userId.equals(id) ? userMapper.toUserDtoWithPassword(user) : userMapper.toUserDto(user);
        log.debug("User with id = '{}' was found", userId);
        return userDto;
    }

    public List<UserDto> getUsers(Long userId, Pageable pageable) {
        List<User> users = userRepository.findAll(pageable).toList();
        log.debug("Found '{}' users", users.size());
        return userMapper.toUserDtoList(users);
    }

    public void deleteUser(Long userId, String userPassword) {
        User user = getUserById(userId);
        verifyPassword(userPassword, user.getPassword());
        userRepository.deleteById(userId);
        log.info("User with id = '{}' was deleted", userId);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User id = " + userId + " not found!"));
    }

    private void checkPasswordStrength(String password) {
        if (!passwordUtils.isPasswordStrong(password)) {
            throw new ValidationException(passwordUtils.getNotStrongPasswordText());
        }
    }

    private void verifyPassword(String password, String hashedPassword) {
        if (!passwordUtils.verifyPassword(password, hashedPassword)) {
            throw new ValidationException(passwordUtils.getWrongPasswordText());
        }
    }
}
