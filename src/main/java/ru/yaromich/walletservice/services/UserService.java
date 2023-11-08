package ru.yaromich.walletservice.services;

import org.springframework.stereotype.Service;
import ru.yaromich.walletservice.domain.dtos.JwtRequest;
import ru.yaromich.walletservice.domain.dtos.RegisterDto;
import ru.yaromich.walletservice.domain.dtos.UserDto;
import ru.yaromich.walletservice.logic.services.exceptions.UserException;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    Optional<UserDto> findByLogin(String login) throws UserException;
    List<UserDto> findAll();
    void save(RegisterDto registerDto) throws UserException;
    void update(UserDto userDto);
    String authenticate(JwtRequest jwtRequest) throws UserException;
}
