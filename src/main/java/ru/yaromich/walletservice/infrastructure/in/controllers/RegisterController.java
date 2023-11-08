package ru.yaromich.walletservice.infrastructure.in.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yaromich.walletservice.domain.dtos.RegisterDto;
import ru.yaromich.walletservice.domain.dtos.RegisterResponse;
import ru.yaromich.walletservice.logic.services.exceptions.AppError;
import ru.yaromich.walletservice.logic.services.exceptions.UserException;
import ru.yaromich.walletservice.services.UserService;

@RestController
@RequestMapping("/register")
@Tag(name = "Регистрация", description = "Методы для работы с регистрацией")
public class RegisterController {
    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Запрос на регистрацию нового пользователя",
            responses = {
                    @ApiResponse(
                            description = "Пользователь успешно зарегистрирован",
                            responseCode = "201",
                            content = @Content(schema = @Schema(implementation = RegisterResponse.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка при регистрации. Такой пользователь уже существует в системе",
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        try {
            userService.save(registerDto);
        } catch (UserException e) {
            return new ResponseEntity<>(new AppError("REGISTER_USER_ERROR", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new RegisterResponse("Successfully registered"));
    }
}
