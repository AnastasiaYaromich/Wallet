package com.yaromich.wallet.infrastructure.in.controllers;

import com.yaromich.wallet.domain.dto.RegisterResponse;
import com.yaromich.wallet.domain.dto.RegisterUserDto;
import com.yaromich.wallet.logic.exceptions.AppError;
import com.yaromich.wallet.logic.exceptions.UserException;
import com.yaromich.wallet.logic.services.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RoleNotFoundException;

@Api("Методы для регистрации пользователя в системе")
@RestController
@RequestMapping("/register")
public class RegistrationController {
    private final UserServiceImpl userService;

    public RegistrationController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @ApiOperation("Запрос на регистрацию")
    @ApiResponses({
            @ApiResponse(code = 200, response = RegisterResponse.class, message = "Пользователь успешно зарегистрирован"),
            @ApiResponse(code = 401, response = AppError.class, message = "Пользователь уже существует")
    })
    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegisterUserDto registerUserDto) {
        try {
            userService.save(registerUserDto);
        } catch (RoleNotFoundException | UserException e) {
            return new ResponseEntity<>(new AppError("REGISTER_USER_ERROR", "User with these " +
                    "credentials already existed"), HttpStatus.BAD_REQUEST);

        }
        return ResponseEntity.ok(new RegisterResponse("Successfully registered"));
    }
}
