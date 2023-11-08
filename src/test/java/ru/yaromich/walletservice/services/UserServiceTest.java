package ru.yaromich.walletservice.services;


import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.yaromich.walletservice.domain.dtos.JwtRequest;
import ru.yaromich.walletservice.domain.dtos.RegisterDto;
import ru.yaromich.walletservice.domain.dtos.UserDto;
import ru.yaromich.walletservice.domain.mappers.UserMapper;
import ru.yaromich.walletservice.domain.models.Role;
import ru.yaromich.walletservice.domain.models.User;
import ru.yaromich.walletservice.logic.services.UserServiceImpl;
import ru.yaromich.walletservice.logic.services.exceptions.BadCredentialsException;
import ru.yaromich.walletservice.logic.services.exceptions.UserAlreadyExistException;
import ru.yaromich.walletservice.logic.services.exceptions.UserException;
import ru.yaromich.walletservice.logic.services.exceptions.UserNotFoundException;
import ru.yaromich.walletservice.repositories.RoleRepository;
import ru.yaromich.walletservice.repositories.UserRepository;
import ru.yaromich.walletservice.repositories.UserRoleRepository;
import ru.yaromich.walletservice.util.JwtTokenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceTest {
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;
    private JwtTokenUtil jwtTokenUtil;
    private UserService userService;
    private JwtRequest jwtRequest;
    private RegisterDto registerDto;
    private User user;
    private UserDto userDto;
    private Role role;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userRoleRepository = Mockito.mock(UserRoleRepository.class);
        roleRepository = Mockito.mock(RoleRepository.class);
        userMapper = Mockito.mock(UserMapper.class);
        jwtTokenUtil = Mockito.mock(JwtTokenUtil.class);
        userService = new UserServiceImpl(userRepository, userRoleRepository, roleRepository, jwtTokenUtil, userMapper);

        role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        user = new User();
        user.setId(1L);
        user.setLogin("Cat with the guitar");
        user.setPassword("999");
        user.setRoles(List.of(role));
        user.setTransactions(List.of());
        user.setAudits(List.of());

        userDto = new UserDto();
        registerDto = new RegisterDto(user.getLogin(), user.getPassword());
        jwtRequest = new JwtRequest(user.getLogin(), user.getPassword());
    }

    @Test
    public void shouldSaveNewUser() throws UserException {
        Mockito.when(userRepository.findByLogin(registerDto.getLogin())).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findByName(Mockito.any())).thenReturn( Optional.of(role));
    }

    @Test
    public void shouldNotSaveUser() throws UserException {
        Mockito.when(userRepository.findByLogin(registerDto.getLogin())).thenReturn(Optional.of(user));
        Assertions.assertThrows(UserAlreadyExistException.class, () -> {
            userService.save(registerDto);
        });
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    @Test
    public void shouldAuthenticateUser() throws UserException {
        String token1 = "some_token";
        Mockito.when(userRepository.findByLogin(jwtRequest.getLogin())).thenReturn(Optional.of(user));
        Mockito.when(jwtTokenUtil.generateToken(user)).thenReturn(token1);

        String token2 = userService.authenticate(jwtRequest);
        AssertionsForClassTypes.assertThat(token1).isEqualTo(token2);
        Mockito.verify(userRepository).findByLogin(jwtRequest.getLogin());
    }

    @Test
    public void shouldNotAuthenticateUserIncorrectLogin() {
        String message = "Incorrect login";
        Mockito.when(userRepository.findByLogin(jwtRequest.getLogin())).thenReturn(Optional.empty());
        BadCredentialsException e = Assertions.assertThrows(BadCredentialsException.class, () -> {
            userService.authenticate(jwtRequest);
        });
        AssertionsForClassTypes.assertThat(e.getMessage()).isEqualTo(message);
        Mockito.verify(userRepository).findByLogin(jwtRequest.getLogin());
    }

    @Test
    public void shouldNotAuthenticateUserIncorrectPassword() {
        String message = "Incorrect password";
        jwtRequest.setPassword("123");
        Mockito.when(userRepository.findByLogin(jwtRequest.getLogin())).thenReturn(Optional.of(user));
        BadCredentialsException e = Assertions.assertThrows(BadCredentialsException.class, () -> {
            userService.authenticate(jwtRequest);
        });
        AssertionsForClassTypes.assertThat(e.getMessage()).isEqualTo(message);
        Mockito.verify(userRepository).findByLogin(jwtRequest.getLogin());
    }

    @Test
    public void shouldFindByLogin() throws UserException {
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        Mockito.when(userMapper.userToUserDto(user)).thenReturn(userDto);

        userService.findByLogin(user.getLogin());
        Mockito.verify(userRepository).findByLogin(user.getLogin());
    }

    @Test
    public void shouldNotFindByLogin() {
        String message = "User not found";
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.empty());
        UserNotFoundException e = Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.findByLogin(user.getLogin());
        });
        AssertionsForClassTypes.assertThat(e.getMessage()).isEqualTo(message);
        Mockito.verify(userRepository).findByLogin(user.getLogin());
    }

    @Test
    public void shouldReturnAllUsers() {
        List<User> users = new ArrayList<>(List.of(user));
        Mockito.when(userRepository.findAll()).thenReturn(users);
        List<UserDto> userDtos = userService.findAll();
        AssertionsForClassTypes.assertThat(userDtos.size()).isEqualTo(users.size());
    }

    @Test
    public void shouldReturnEmptyUsersList() {
        List<User> users = new ArrayList<>();
        Mockito.when(userRepository.findAll()).thenReturn(new ArrayList<>());
        List<UserDto> userDtos = userService.findAll();
        AssertionsForClassTypes.assertThat(userDtos.size()).isEqualTo(0);
    }
}
