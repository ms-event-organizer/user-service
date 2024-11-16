package meetup.user_service.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meetup.user_service.exception.NotFoundException;
import meetup.user_service.exception.ValidationException;
import meetup.user_service.user.util.PasswordUtils;
import meetup.user_service.user.mapper.UserMapper;
import meetup.user_service.user.dao.UserRepository;
import meetup.user_service.user.dto.NewUserRequest;
import meetup.user_service.user.dto.UpdateUserRequest;
import meetup.user_service.user.dto.UserDto;
import meetup.user_service.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static meetup.user_service.user.util.PasswordUtils.hashPassword;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserDto createUser(Long userId, NewUserRequest newUserRequest) {
        checkPasswordStrength(newUserRequest.getPassword());
        User newUser = userRepository.save(
                User.builder()
                        .name(newUserRequest.getName())
                        .email(newUserRequest.getEmail())
                        .password(hashPassword(newUserRequest.getPassword()))
                        .aboutMe(newUserRequest.getAboutMe())
                        .build()
        );
        log.info("User with id = '{}' was created", newUser.getId());
        return UserMapper.toUserDto(newUser);
    }

    public UserDto updateUser(Long userId, String userPassword, UpdateUserRequest updateUserRequest) {
        User user = getUserById(userId);
        verifyPassword(userPassword, user.getPassword());
        if (updateUserRequest.getName() != null && !updateUserRequest.getName().isBlank()) {
            user.setName(updateUserRequest.getName());
        }
        if (updateUserRequest.getAboutMe() != null && !updateUserRequest.getAboutMe().isBlank()) {
            user.setAboutMe(updateUserRequest.getAboutMe());
        }
        if (updateUserRequest.getPassword() != null) {
            checkPasswordStrength(updateUserRequest.getPassword());
            user.setPassword(updateUserRequest.getPassword());
        }
        userRepository.save(user);
        log.info("User with id = '{}' was updated", user.getId());
        return UserMapper.toUserDto(user);
    }

    public UserDto getUser(Long userId, Long id) {
        User user = getUserById(userId);
        UserDto userDto = UserMapper.toUserDto(user);
        if (userId.equals(id)) userDto.setPassword(user.getPassword());
        log.debug("User with id = '{}' was found", userId);
        return userDto;
    }

    public List<UserDto> getUsers(Long userId, Pageable pageable) {
        List<User> users = userRepository.findAll(pageable).toList();
        log.debug("Found '{}' users", users.size());
        return UserMapper.toUserDtoList(users);
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
        if (!PasswordUtils.isPasswordStrong(password)) {
            throw new ValidationException(PasswordUtils.NOT_STRONG_PASSWORD);
        }
    }

    private void verifyPassword(String password, String hashedPassword) {
        if (!PasswordUtils.verifyPassword(password, hashedPassword)) {
            throw new ValidationException(PasswordUtils.WRONG_PASSWORD);
        }
    }
}
