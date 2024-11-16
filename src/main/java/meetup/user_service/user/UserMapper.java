package meetup.user_service.user;

import meetup.user_service.user.dto.UserDto;
import meetup.user_service.user.model.User;

import java.util.List;

public class UserMapper {

    public static List<UserDto> toUserDtoList(List<User> users) {
        if (users == null) return List.of();
        return users.stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .aboutMe(user.getAboutMe())
                .build();
    }

}
