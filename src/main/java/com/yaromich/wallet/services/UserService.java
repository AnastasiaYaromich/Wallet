package com.yaromich.wallet.services;

import com.yaromich.wallet.domain.dto.RegisterUserDto;
import com.yaromich.wallet.domain.dto.UserDto;
import com.yaromich.wallet.logic.exceptions.UserException;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDto> findUserByLogin(String login) throws UserException;
    void save(RegisterUserDto registerUserDto) throws UserException, RoleNotFoundException;
    List<UserDto> findAll() throws UserException;
    void update(UserDto userDto) throws UserException;
}

