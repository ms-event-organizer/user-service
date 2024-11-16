package meetup.user_service.user;

import meetup.user_service.user.dto.NewUserDto;
import meetup.user_service.user.dto.UserDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserDto createUser(Long userId, NewUserDto newUserDto);

    UserDto updateUser(Long userId, String userPassword, NewUserDto newUserDto);

    UserDto getUser(Long userId, Long id);

    List<UserDto> getUsers(Long userId, Pageable pageable);

    void deleteUser(Long userId, String userPassword);
}
