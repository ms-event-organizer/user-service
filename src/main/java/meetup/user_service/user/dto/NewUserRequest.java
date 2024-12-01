package meetup.user_service.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import meetup.user_service.user.util.StrongPassword;

@Schema(description = "New user request")
public record NewUserRequest(
        @NotBlank(message = "Name can not be blank")
        @Size(min = 3, max = 254, message = "The name length must be greater than 3 and less than 254")
        @Schema(description = "User name")
        String name,
        @Email(message = "E-mail is not correct")
        @Size(min = 6, max = 254, message = "The e-mail length must be greater than 6 and less than 254")
        @Schema(description = "User email")
        String email,
        @NotBlank(message = "Password can not be blank")
        @StrongPassword
        @Schema(description = "User password")
        String password,
        @Schema(description = "User about me")
        String aboutMe
) {
}
