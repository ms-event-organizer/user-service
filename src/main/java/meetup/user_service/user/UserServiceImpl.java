package meetup.user_service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meetup.user_service.exception.NotFoundException;
import meetup.user_service.exception.ValidationException;
import meetup.user_service.user.dao.UserRepository;
import meetup.user_service.user.dto.NewUserDto;
import meetup.user_service.user.dto.UserDto;
import meetup.user_service.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static meetup.user_service.user.PasswordUtils.hashPassword;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserDto createUser(Long userId, NewUserDto newUserDto) {
        checkPasswordStrength(newUserDto.getPassword());
        User newUser = userRepository.save(
                User.builder()
                        .name(newUserDto.getName())
                        .email(newUserDto.getEmail())
                        .password(hashPassword(newUserDto.getPassword()))
                        .aboutMe(newUserDto.getAboutMe())
                        .build()
        );
        log.info("User with id = '{}' was created", newUser.getId());
        return UserMapper.toUserDto(newUser);
    }

    public UserDto updateUser(Long userId, String userPassword, NewUserDto newUserDto) {
        User user = getUserById(userId);
        verifyPassword(userPassword, user.getPassword());
        if (newUserDto.getName() != null && !newUserDto.getName().isBlank()) {
            user.setName(newUserDto.getName());
        }
        if (newUserDto.getAboutMe() != null && !newUserDto.getAboutMe().isBlank()) {
            user.setAboutMe(newUserDto.getAboutMe());
        }
        if (newUserDto.getPassword() != null) {
            checkPasswordStrength(newUserDto.getPassword());
            user.setPassword(newUserDto.getPassword());
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
