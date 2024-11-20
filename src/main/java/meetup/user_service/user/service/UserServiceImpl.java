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
import org.springframework.data.domain.PageRequest;
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

    @Override
    public UserDto createUser(Long userId, NewUserRequest newUserRequest) {
        User newUser = userMapper.toUser(newUserRequest);
        newUser.setPassword(passwordUtils.hashPassword(newUser.getPassword()));
        userRepository.save(newUser);
        log.info("User with id = '{}' was created", newUser.getId());
        return userMapper.toUserDtoWithPassword(newUser);
    }

    @Override
    public UserDto updateUser(Long userId, String userPassword, UpdateUserRequest updateUserRequest) {
        User user = getUserById(userId);
        verifyPassword(userPassword, user.getPassword());
        userMapper.updateUser(updateUserRequest, user);
        if (updateUserRequest.password() != null){
            user.setPassword(passwordUtils.hashPassword(updateUserRequest.password()));
        }
        userRepository.save(user);
        log.info("User with id = '{}' was updated", user.getId());
        return userMapper.toUserDtoWithPassword(user);
    }

    @Override
    public UserDto getUser(Long userId, Long id) {
        User user = getUserById(id);
        UserDto userDto = userId.equals(id) ? userMapper.toUserDtoWithPassword(user) : userMapper.toUserDto(user);
        log.debug("User with id = '{}' was found", id);
        return userDto;
    }

    @Override
    public List<UserDto> getUsers(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<User> users = userRepository.findAll(pageable).toList();
        log.debug("Found '{}' users", users.size());
        return userMapper.toUserDtoList(users);
    }

    @Override
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

    private void verifyPassword(String password, String hashedPassword) {
        if (!passwordUtils.verifyPassword(password, hashedPassword)) {
            throw new ValidationException(passwordUtils.getWrongPasswordText());
        }
    }
}
