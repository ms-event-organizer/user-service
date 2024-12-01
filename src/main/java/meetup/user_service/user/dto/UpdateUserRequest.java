package meetup.user_service.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import meetup.user_service.user.util.StrongPassword;

/**
 * @param name
 * @param password
 * @param aboutMe
 */
@Schema(description = "Update User Request")
public record UpdateUserRequest(
        @Pattern(regexp = "^(?!\\s*$).+", message = "Name can not be empty")
        @Size(min = 3, max = 254, message = "The name length must be greater than 3 and less than 254")
        @Schema(description = "User name")
        String name,
        @StrongPassword
        @Schema(description = "User password")
        String password,
        @Schema(description = "User about me")
        String aboutMe
) {
}
