package meetup.user_service.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import meetup.user_service.user.util.StrongPassword;

public record UpdateUserRequest(
        @Pattern(regexp = "^(?!\\s*$).+", message = "Name can not be empty")
        @Size(min = 3, max = 254, message = "The name length must be greater than 3 and less than 254")
        String name,
        @StrongPassword
        String password,
        String aboutMe
) {
}
