package mappers;

import domain.models.User;
import dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    UserDto mapUserToDto(User user);
    User mapToUser(UserDto userDto);
}
