package meetup.user_service.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meetup.user_service.user.dto.NewUserRequest;
import meetup.user_service.user.dto.UpdateUserRequest;
import meetup.user_service.user.dto.UserDto;
import meetup.user_service.user.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody @Valid NewUserRequest newUserRequest) {
        log.debug("Creating user '{}' by user id = '{}'", newUserRequest.name(), userId);
        return userService.createUser(userId, newUserRequest);
    }

    @PatchMapping
    public UserDto updateUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestHeader("X-Sharer-User-Password") String userPassword,
                              @RequestBody UpdateUserRequest updateUserRequest) {
        log.debug("Updating user id = '{}'", userId);
        return userService.updateUser(userId, userPassword, updateUserRequest);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable Long id) {
        log.debug("User id = '{}' requests info about user id = '{}'", userId, id);
        return userService.getUser(userId, id);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                  @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        log.debug("User id = '{}' requests info about all users", userId);
        return userService.getUsers(userId, pageable);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestHeader("X-Sharer-User-Password") String userPassword) {
        log.debug("Deleting user id = '{}'", userId);
        userService.deleteUser(userId, userPassword);
    }

}