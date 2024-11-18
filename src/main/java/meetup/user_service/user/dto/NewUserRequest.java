package meetup.user_service.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import meetup.user_service.user.util.StrongPassword;

public record NewUserRequest(
        @NotBlank(message = "Name can not be blank")
        String name,
        @Email(message = "E-mail is not correct")
        String email,
        @NotBlank(message = "Password can not be blank")
        @StrongPassword
        String password,
        String aboutMe
) {
}
