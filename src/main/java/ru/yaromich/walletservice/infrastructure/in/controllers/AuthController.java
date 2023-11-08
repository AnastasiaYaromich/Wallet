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
import ru.yaromich.walletservice.domain.dtos.JwtRequest;
import ru.yaromich.walletservice.domain.dtos.JwtResponse;
import ru.yaromich.walletservice.logic.services.exceptions.AppError;
import ru.yaromich.walletservice.logic.services.exceptions.UserException;
import ru.yaromich.walletservice.services.UserService;

@RestController
@RequestMapping("/authorize")
@Tag(name = "Аутентификация", description = "Методы работы с аутентификацией")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Запрос на авторизацию/создание токена",
            responses = {
                    @ApiResponse(
                            description = "Пользователь прошел авторизацию, токен успешно создан",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = JwtResponse.class))
                    ),
                    @ApiResponse(
                            description = "Некорректный логин или пароль",
                            responseCode = "401",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> authorize(@RequestBody JwtRequest jwtRequest) {
        String token;
        try {
            token = userService.authenticate(jwtRequest);
        } catch (UserException e) {
            return new ResponseEntity<>(new AppError("BAD CREDENTIALS", e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
