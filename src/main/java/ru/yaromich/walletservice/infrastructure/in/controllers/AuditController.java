package ru.yaromich.walletservice.infrastructure.in.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yaromich.walletservice.domain.dtos.AuditDto;
import ru.yaromich.walletservice.logic.services.exceptions.AppError;
import ru.yaromich.walletservice.logic.services.exceptions.UserException;
import ru.yaromich.walletservice.services.AuditService;

import java.util.List;

@RestController
@RequestMapping("/audit")
@Tag(name = "Аудит", description = "Методы для работы с аудитом")
public class AuditController {

    private final AuditService auditService;

    @Autowired
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @Operation(
            summary = "Запрос на получение аудита действий пользователя",
            responses = {
                    @ApiResponse(
                            description = "Аудит действий пользователя найден",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = List.class))
                    ),
                    @ApiResponse(
                            description = "Аудит пользователя не найден",
                            responseCode = "401",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @GetMapping("/{name}")
    public ResponseEntity<?> getAuditByUsername(@PathVariable  @Parameter(description = "Имя клиента", required = true) String name) {
        try {
            List<AuditDto>  audits = auditService.findAllByUserName(name);
            return ResponseEntity.ok(audits);
        } catch (UserException e) {
            return new ResponseEntity<>(new AppError("AUDIT FAILED", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
