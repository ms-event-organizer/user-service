package meetup.user_service.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record NewUserRequest(
        @NotNull String name,
        @Email String email,
        @NotNull String password,
        String aboutMe
) {
}
