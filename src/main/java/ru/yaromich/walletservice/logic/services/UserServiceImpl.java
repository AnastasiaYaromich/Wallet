package ru.yaromich.walletservice.logic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yaromich.walletservice.domain.dtos.JwtRequest;
import ru.yaromich.walletservice.domain.dtos.RegisterDto;
import ru.yaromich.walletservice.domain.dtos.UserDto;
import ru.yaromich.walletservice.domain.mappers.UserMapper;
import ru.yaromich.walletservice.domain.models.Role;
import ru.yaromich.walletservice.domain.models.User;
import ru.yaromich.walletservice.logic.services.exceptions.BadCredentialsException;
import ru.yaromich.walletservice.logic.services.exceptions.UserAlreadyExistException;
import ru.yaromich.walletservice.logic.services.exceptions.UserException;
import ru.yaromich.walletservice.logic.services.exceptions.UserNotFoundException;
import ru.yaromich.walletservice.repositories.RoleRepository;
import ru.yaromich.walletservice.repositories.UserRepository;
import ru.yaromich.walletservice.repositories.UserRoleRepository;
import ru.yaromich.walletservice.services.UserService;
import ru.yaromich.walletservice.util.JwtTokenUtil;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, RoleRepository roleRepository, JwtTokenUtil jwtTokenUtil, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Optional<UserDto> findByLogin(String login) throws UserException {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException("User not found"));
        return Optional.of(UserMapper.MAPPER.userToUserDto(user));
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper.MAPPER::userToUserDto).collect(Collectors.toList());
    }

    @Override
    public void save(RegisterDto registerDto) throws UserException {
        Optional<User> users = userRepository.findByLogin(registerDto.getLogin());
        if(users.isPresent()) {
            throw new UserAlreadyExistException("User already exist");
        }
        Role role = roleRepository.findByName("ROLE_USER").get();

        User savedUser = new User();
        savedUser.setLogin(registerDto.getLogin());
        savedUser.setPassword(registerDto.getPassword());
        savedUser.setBalance(BigDecimal.ZERO);
        savedUser.setRoles(List.of(role));
        savedUser.setTransactions(List.of());
        userRepository.save(savedUser);
        userRoleRepository.save(userRepository.findByLogin(savedUser.getLogin()).get().getId(), role.getId());
    }

    @Override
    public void update(UserDto userDto) {
        User user = UserMapper.MAPPER.userDtoToUser(userDto);
        userRepository.update(user);
    }

    @Override
    public String authenticate(JwtRequest jwtRequest) throws UserException {
        User user = userRepository.findByLogin(jwtRequest.getLogin()).orElseThrow(() -> new BadCredentialsException("Incorrect login"));

        if(!user.getPassword().equals(jwtRequest.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }
        return jwtTokenUtil.generateToken(user);
    }
}
