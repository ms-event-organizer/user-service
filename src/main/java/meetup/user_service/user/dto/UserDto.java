package meetup.user_service.user.dto;

import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String name,
        String email,
        String password,
        String aboutMe
) {
}
