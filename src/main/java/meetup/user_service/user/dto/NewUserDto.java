package meetup.user_service.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {
    @NotNull
    private String name;
    @Email
    private String email;
    @NotNull
    private String password;
    private String aboutMe;
}
