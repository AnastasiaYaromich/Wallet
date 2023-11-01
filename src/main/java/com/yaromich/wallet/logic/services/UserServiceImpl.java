package com.yaromich.wallet.logic.services;

import com.yaromich.wallet.domain.dto.JwtRequest;
import com.yaromich.wallet.domain.dto.RegisterUserDto;
import com.yaromich.wallet.domain.dto.UserDto;
import com.yaromich.wallet.domain.model.Role;
import com.yaromich.wallet.domain.model.User;
import com.yaromich.wallet.infrastructure.out.RoleRepositoryImpl;
import com.yaromich.wallet.infrastructure.out.UserRepositoryImpl;
import com.yaromich.wallet.infrastructure.out.UserRoleRepositoryImpl;
import com.yaromich.wallet.logic.exceptions.UserAlreadyExistException;
import com.yaromich.wallet.logic.exceptions.UserException;
import com.yaromich.wallet.logic.exceptions.UserNotFoundException;
import com.yaromich.wallet.mappers.RoleMapper;
import com.yaromich.wallet.mappers.UserMapper;
import com.yaromich.wallet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepositoryImpl userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepositoryImpl roleRepository;
    private final UserRoleRepositoryImpl userRoleRepository;


    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepository, PasswordEncoder passwordEncoder, RoleRepositoryImpl roleRepository, UserRoleRepositoryImpl userRoleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }


    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> findUserByLogin(String login) throws UserException {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException("User not found"));
        UserDto userDto = UserMapper.MAPPER.userToUserDto(user);
        return Optional.of(userDto);
    }

    @Override
    public void save(RegisterUserDto registerUserDto) throws RoleNotFoundException, UserException{
        Optional<User> existedUser = userRepository.findByLogin(registerUserDto.getLogin());
        if(existedUser.isPresent()) {
            throw new UserAlreadyExistException("User already exist");
        }

        String bcryptPasswordEncoder = passwordEncoder.encode(registerUserDto.getPassword());
        Role role = roleRepository.findByName(registerUserDto.getRole()).orElseThrow(() -> new RoleNotFoundException("Role not found"));

        UserDto userDto = new UserDto();
        userDto.setLogin(registerUserDto.getLogin());
        userDto.setPassword(bcryptPasswordEncoder);
        userDto.setRoles(List.of(RoleMapper.MAPPER.roleToRoleDto(role)));
        userDto.setBalance(BigDecimal.ZERO);

        User user = UserMapper.MAPPER.userDtoToUser(userDto);
        userRepository.save(user);

        User foundedUser = userRepository.findByLogin(user.getLogin()).orElseThrow(() -> new UserNotFoundException("User not found"));
        userRoleRepository.save(foundedUser.getId(), role.getId());
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        for (User user: users) {
            user.setRoles(userRoleRepository.findAllByUserId(user.getId()));
        }
        return users.stream().map(UserMapper.MAPPER::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void update(UserDto userDto) {
        User user = UserMapper.MAPPER.userDtoToUser(userDto);
        userRepository.update(user);
    }


    public void authenticate(JwtRequest jwtRequest) throws UserException {
        User user = userRepository.findByLogin(jwtRequest.getLogin()).orElseThrow(() ->
                new UserNotFoundException("User with this login not found"));
        if(!user.getPassword().equals(jwtRequest.getPassword())) {
            throw new UserNotFoundException("Invalid password");
        }
    }
}
