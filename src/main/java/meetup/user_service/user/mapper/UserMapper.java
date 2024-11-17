package meetup.user_service.user.mapper;

import meetup.user_service.user.dto.UserDto;
import meetup.user_service.user.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public List<UserDto> toUserDtoList(List<User> users) {
        if (users == null) return List.of();
        return users.stream()
                .map(this::toUserDto)
                .toList();
    }

    public UserDto toUserDto(User user) {
        if (user == null) return null;
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .aboutMe(user.getAboutMe())
                .build();
    }

    public UserDto toUserDtoWithPassword(User user) {
        if (user == null) return null;
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .aboutMe(user.getAboutMe())
                .build();
    }
}

