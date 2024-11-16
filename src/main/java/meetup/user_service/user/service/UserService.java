package meetup.user_service.user.service;

import meetup.user_service.user.dto.NewUserRequest;
import meetup.user_service.user.dto.UpdateUserRequest;
import meetup.user_service.user.dto.UserDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserDto createUser(Long userId, NewUserRequest newUserRequest);

    UserDto updateUser(Long userId, String userPassword, UpdateUserRequest updateUserRequest);

    UserDto getUser(Long userId, Long id);

    List<UserDto> getUsers(Long userId, Pageable pageable);

    void deleteUser(Long userId, String userPassword);
}
