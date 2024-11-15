package meetup.user_service.user;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import meetup.user_service.user.dto.NewUserDto;
import meetup.user_service.user.dto.UserDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody NewUserDto newUserDto) {
        return userService.createUser(userId, newUserDto);
    }

    @PatchMapping
    public UserDto updateUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestHeader("X-Sharer-User-Password") String userPassword,
                              @RequestBody NewUserDto newUserDto) {
        return userService.updateUser(userId, userPassword, newUserDto);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable Long id) {
        return userService.getUser(userId, id);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                  @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        return userService.getUsers(userId, pageable);
    }

    @DeleteMapping
    public void deleteUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestHeader("X-Sharer-User-Password") String userPassword) {
        userService.deleteUser(userId, userPassword);
    }

}