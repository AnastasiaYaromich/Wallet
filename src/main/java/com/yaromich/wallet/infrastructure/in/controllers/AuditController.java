package com.yaromich.wallet.infrastructure.in.controllers;

import com.yaromich.wallet.config.JwtRequestFilter;
import com.yaromich.wallet.domain.dto.AuditDto;
import com.yaromich.wallet.logic.exceptions.AppError;
import com.yaromich.wallet.logic.exceptions.UserException;
import com.yaromich.wallet.logic.services.AuditServiceImpl;
import com.yaromich.wallet.logic.services.UserServiceImpl;
import com.yaromich.wallet.utils.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api("Методы для просмотра действий пользователя")
@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditServiceImpl auditService;
    private final UserServiceImpl userService;
    private final JwtRequestFilter jwtRequestFilter;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuditController(AuditServiceImpl auditService, UserServiceImpl userService, JwtRequestFilter jwtRequestFilter, JwtTokenUtil jwtTokenUtil) {
        this.auditService = auditService;
        this.userService = userService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @ApiOperation("Запрос на просмотр действий пользователя в банковский системе")
    @ApiResponses({
            @ApiResponse(code = 401, response = AppError.class, message = "Пользователь не авторизован в системе. " +
                    " Просмотр аудита действий невозможен"),
            @ApiResponse(code = 400, response = AppError.class, message = "У вас нет прав для просмотра аудита дейсвий"),
            @ApiResponse(code = 200, response = Map.class, message = "Аудит действий пользователя найден")
    })
    @GetMapping("/{name}")
    public ResponseEntity<?> getAuditByUsername(@PathVariable String name, HttpServletRequest request) throws ServletException, IOException {
        List<AuditDto> audits = new ArrayList<>();
        if(jwtRequestFilter.filter(request)) {
            List<String> roles = jwtTokenUtil.getRolesFromToken(request.getHeader("authorization").substring(7));

            for (String role: roles) {
                System.out.println(role);
                if(role.equals("ROLE_ADMIN")) {
                    try {
                        audits = auditService.findAllByUserName(name);
                        return ResponseEntity.ok(audits);
                    } catch (UserException e) {
                        return new ResponseEntity<>(new AppError("AUDIT FAILED", e.getMessage()), HttpStatus.BAD_REQUEST);
                    }
                }
                return new ResponseEntity<>(new AppError("AUDIT FAILED", "YOU DON'T HAVE PERMISSIONS TO DO THAT"), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new AppError("NOT ALLOWED", "UNAUTHORIZED"), HttpStatus.UNAUTHORIZED);
    }
}
