package meetup.user_service.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import meetup.user_service.user.util.StrongPassword;

public record NewUserRequest(
        @NotBlank(message = "Name can not be blank")
        @Size(min = 3, max = 254, message = "The name length must be greater than 3 and less than 254")
        String name,
        @Email(message = "E-mail is not correct")
        @Size(min = 6, max = 254, message = "The e-mail length must be greater than 6 and less than 254")
        String email,
        @NotBlank(message = "Password can not be blank")
        @StrongPassword
        String password,
        String aboutMe
) {
}
