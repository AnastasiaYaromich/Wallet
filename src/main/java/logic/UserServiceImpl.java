package logic;

import aop.annotations.Speed;
import domain.models.User;
import dto.UserDto;
import mappers.UserMapper;
import repositories.UserRepository;
import services.UserService;
import services.exceptions.UserException;
import services.exceptions.UserIsNotValidException;
import services.exceptions.UserNotFoundException;
import services.exceptions.ValidationException;
import validation.UserValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Speed
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto findUserByLogin(String login) throws UserException {
        Optional<User> user = userRepository.findUserByLogin(login);
        User foundedUser;
        if(user.isPresent()) {
            foundedUser = user.get();
            return UserMapper.MAPPER.mapUserToDto(foundedUser);
        }
        throw new UserNotFoundException("User does not exist");
    }

    @Override
    public UserDto save(UserDto userDto) throws ValidationException {
        userDto.setBalance(BigDecimal.ZERO);
        userDto.setRole("user");

        boolean isValid = UserValidator.isValid(userDto);
        if(isValid) {
            User user = UserMapper.MAPPER.mapToUser(userDto);
            User savedUser = userRepository.save(user);
            return UserMapper.MAPPER.mapUserToDto(savedUser);
        }
            throw new UserIsNotValidException("Bad credentials. User credentials should" +
                    " have not null and between 6 and 9 characters.");
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper.MAPPER::mapUserToDto)
                .collect(Collectors.toList());
    }
}
