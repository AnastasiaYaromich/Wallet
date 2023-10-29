package services;

import dto.UserDto;
import services.exceptions.UserException;
import services.exceptions.ValidationException;
import java.util.List;

public interface UserService {
    UserDto findUserByLogin(String login) throws UserException;
    UserDto save(UserDto userDto) throws ValidationException;
    List<UserDto> findAll();
}

