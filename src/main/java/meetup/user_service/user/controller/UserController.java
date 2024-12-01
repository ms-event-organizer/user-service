package meetup.user_service.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meetup.user_service.user.dto.NewUserRequest;
import meetup.user_service.exception.ErrorResponse;
import meetup.user_service.user.dto.UpdateUserRequest;
import meetup.user_service.user.dto.UserDto;
import meetup.user_service.user.service.UserService;
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

  @Operation(summary = "Create user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User was created", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
            }),
    @ApiResponse(responseCode = "400", description = "Validation error", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
    @ApiResponse(responseCode = "500", description = "Unknown error", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestHeader(value = "X-User-Id", required = false) Long userId,
                              @RequestBody @Valid NewUserRequest newUserRequest) {
        log.debug("Creating user '{}' by user id = '{}'", newUserRequest.name(), userId);
        return userService.createUser(userId, newUserRequest);
    }
  @Operation(summary = "Update user")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "User was updated", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
            }),
    @ApiResponse(responseCode = "400", description = "Validation error", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
    @ApiResponse(responseCode = "400", description = "Wrong password!", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
    @ApiResponse(responseCode = "404", description = "User is not found", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
    @ApiResponse(responseCode = "500", description = "Unknown error", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @PatchMapping
    public UserDto updateUser(@RequestHeader("X-User-Id") Long userId,
                              @RequestHeader("X-User-Password") String userPassword,
                              @RequestBody @Valid UpdateUserRequest updateUserRequest) {
        log.debug("Updating user id = '{}'", userId);
        return userService.updateUser(userId, userPassword, updateUserRequest);
    }

  @Operation(summary = "Find user by id")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "User was found", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
            }),
    @ApiResponse(responseCode = "404", description = "User is not found", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
    @ApiResponse(responseCode = "500", description = "Unknown error", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping("/{id}")
    public UserDto getUser(@RequestHeader("X-User-Id") Long userId,
                           @PathVariable Long id) {
        log.debug("User id = '{}' requests info about user id = '{}'", userId, id);
        return userService.getUser(userId, id);
    }

  @Operation(summary = "Get users")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Found users", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
            }),
    @ApiResponse(responseCode = "500", description = "Unknown error", content = {
              @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @GetMapping
    public List<UserDto> getUsers(@RequestHeader("X-User-Id") Long userId,
                                  @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                  @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.debug("User id = '{}' requests info about all users", userId);
        return userService.getUsers(userId, from, size);
    }

  @Operation(summary = "Delete user")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "User was deleted"),
    @ApiResponse(responseCode = "400", description = "Wrong password!", content = {
             @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
    @ApiResponse(responseCode = "404", description = "User not found", content = {
             @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
    @ApiResponse(responseCode = "500", description = "Unknown error", content = {
             @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestHeader("X-User-Id") Long userId,
                           @RequestHeader("X-User-Password") String userPassword) {
        log.debug("Deleting user id = '{}'", userId);
        userService.deleteUser(userId, userPassword);
    }

}