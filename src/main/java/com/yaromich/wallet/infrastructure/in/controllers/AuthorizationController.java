package com.yaromich.wallet.infrastructure.in.controllers;

import com.yaromich.wallet.domain.dto.JwtRequest;
import com.yaromich.wallet.domain.dto.JwtResponse;
import com.yaromich.wallet.logic.exceptions.AppError;
import com.yaromich.wallet.logic.exceptions.UserException;
import com.yaromich.wallet.logic.services.UserServiceImpl;
import com.yaromich.wallet.utils.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("Методы для авторизации пользователя в системе")
@RestController
@RequestMapping("/authorize")
public class AuthorizationController {
    private final UserServiceImpl userService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthorizationController(UserServiceImpl userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @ApiOperation("Запрос на создание токена")
    @ApiResponses({
            @ApiResponse(code = 201, response = JwtResponse.class, message = "Токен успешно создан"),
            @ApiResponse(code = 401, response = AppError.class, message = "Некорректный логин или пароль")
    })
    @PostMapping
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest jwtRequest) {
        try {
            userService.authenticate(jwtRequest);
            UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getLogin());
            String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (UserException e) {
            return new ResponseEntity<>(new AppError("CHECK_TOKEN_ERROR", e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}
