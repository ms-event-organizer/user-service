package meetup.user_service.user.dto;

import jakarta.validation.constraints.Pattern;
import meetup.user_service.user.util.StrongPassword;

public record UpdateUserRequest(
        @Pattern(regexp = "^(?!\\s*$).+", message = "Name can not be empty")
        String name,
        @StrongPassword
        String password,
        String aboutMe
) {
}
