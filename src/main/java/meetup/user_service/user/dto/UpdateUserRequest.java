package meetup.user_service.user.dto;

public record UpdateUserRequest(
        String name,
        String password,
        String aboutMe
        ) {
}
