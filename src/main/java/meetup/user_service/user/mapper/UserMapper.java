package meetup.user_service.user.mapper;

import meetup.user_service.user.dto.NewUserRequest;
import meetup.user_service.user.dto.UpdateUserRequest;
import meetup.user_service.user.dto.UserDto;
import meetup.user_service.user.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //@Mapping(target = "id", ignore = true)
    User toUser(NewUserRequest newUserRequest);

    @IterableMapping(qualifiedByName = "toUserDto")
    List<UserDto> toUserDtoList(List<User> users);

    @Named("toUserDto")
    @Mapping(target = "password", ignore = true)
    UserDto toUserDto(User user);

    @Named("toUserDtoWithPassword")
    UserDto toUserDtoWithPassword(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateUser(UpdateUserRequest updateUserRequest, @MappingTarget User userToUpdate);
}