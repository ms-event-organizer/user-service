package meetup.user_service.user;

import lombok.RequiredArgsConstructor;
import meetup.user_service.exception.NotFoundException;
import meetup.user_service.exception.ValidationException;
import meetup.user_service.user.dao.UserRepository;
import meetup.user_service.user.dto.NewUserDto;
import meetup.user_service.user.dto.UserDto;
import meetup.user_service.user.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static meetup.user_service.user.PasswordUtils.NOT_STRONG_PASSWORD;
import static meetup.user_service.user.PasswordUtils.hashPassword;
import static meetup.user_service.user.PasswordUtils.isPasswordStrong;
import static meetup.user_service.user.PasswordUtils.verifyPassword;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto createUser(Long userId, NewUserDto newUserDto) {
        if (!isPasswordStrong(newUserDto.getPassword())) {
            throw new ValidationException("Password is not strong: at least 8 characters, one uppercase, one lowercase, one digit, and one special character \"!@#$%^&*()-+\".");
        }
        return UserMapper.toUserDto(
                userRepository.save(
                        User.builder()
                                .name(newUserDto.getName())
                                .email(newUserDto.getEmail())
                                .password(hashPassword(newUserDto.getPassword()))
                                .aboutMe(newUserDto.getAboutMe())
                                .build()
                )
        );
    }

    public UserDto updateUser(Long userId, String userPassword, NewUserDto newUserDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User id = " + userId + " not found!")
        );
        if (!verifyPassword(userPassword, user.getPassword())) {
            throw new ValidationException("Wrong password!");
        }
        if (newUserDto.getName() != null && !newUserDto.getName().isBlank()) {
            user.setName(newUserDto.getName());
        }
        if (newUserDto.getAboutMe() != null && !newUserDto.getAboutMe().isBlank()) {
            user.setAboutMe(newUserDto.getAboutMe());
        }
        if (newUserDto.getPassword() != null && isPasswordStrong(newUserDto.getPassword())) {
            user.setPassword(newUserDto.getPassword());
        } else
            throw new ValidationException(NOT_STRONG_PASSWORD);
        return UserMapper.toUserDto(
                userRepository.save(user)
        );
    }

    public UserDto getUser(Long userId, Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User id = " + id + " not found!")
        );
        UserDto userDto = UserMapper.toUserDto(user);
        if (userId.equals(id)) userDto.setPassword(user.getPassword());
        return userDto;
    }

    public List<UserDto> getUsers(Long userId, Pageable pageable) {
        return UserMapper.toUserDtoList(
                userRepository.findAll(pageable).toList()
        );
    }

    public void deleteUser(Long userId, String userPassword) {
        if (!isPasswordStrong(userPassword)) {
            throw new ValidationException(NOT_STRONG_PASSWORD);
        }
        userRepository.deleteById(userId);
    }
}
