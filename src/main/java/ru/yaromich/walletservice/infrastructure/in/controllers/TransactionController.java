package ru.yaromich.walletservice.infrastructure.in.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yaromich.walletservice.domain.dtos.TransactionDto;
import ru.yaromich.walletservice.domain.dtos.TransactionRequest;
import ru.yaromich.walletservice.domain.dtos.TransactionResponse;
import ru.yaromich.walletservice.logic.services.exceptions.AppError;
import ru.yaromich.walletservice.logic.services.exceptions.TransactionException;
import ru.yaromich.walletservice.logic.services.exceptions.UserException;
import ru.yaromich.walletservice.services.TransactionService;
import ru.yaromich.walletservice.util.JwtTokenUtil;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/transaction")
@Tag(name ="Транзации", description = "Методы для работы с транзакциями")
public class TransactionController {
    private final TransactionService transactionService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public TransactionController(TransactionService transactionService, JwtTokenUtil jwtTokenUtil) {
        this.transactionService = transactionService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Operation(
            summary = "Запрос на снятие денежных средств",
            responses = {
                    @ApiResponse(
                            description = "Транзакция прошла успешно",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TransactionResponse.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка при выполнении транзации",
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawMoney(@RequestBody TransactionRequest transactionRequest) {
        try {
            transactionService.withdraw(transactionRequest);
        } catch (TransactionException | UserException e) {
            return new ResponseEntity<>(new AppError("TRANSACTION_FAILED", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new TransactionResponse("WITHDRAWAL WAS SUCCESSFUL"));
    }


    @Operation(
            summary = "Запрос на пополнение денежных средств",
            responses = {
                    @ApiResponse(
                            description = "Транзакция прошла успешно",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TransactionResponse.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка при выполнении транзации",
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @PostMapping("/replenish")
    public ResponseEntity<?> replenishMoney(@RequestBody TransactionRequest transactionRequest) {
        try {
            transactionService.replenish(transactionRequest);
        } catch (TransactionException | UserException e) {
            return new ResponseEntity<>(new AppError("TRANSACTION FAILED", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new TransactionResponse("REPLENISH WAS SUCCESSFUL"));
    }

    @Operation(
            summary = "Запрос на получение истории предыдущих транцакций",
            responses = {
                    @ApiResponse(
                            description = "История транзакций пользователя найдена",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = List.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка при получении истории",
                            responseCode = "400",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @GetMapping("/history")
    public ResponseEntity<?> history(HttpServletRequest request) throws IOException {
        String token = jwtTokenUtil.extractToken(request);
        List<TransactionDto> transactions = null;
        try {
            transactions = transactionService.findAllByUserLogin(jwtTokenUtil.getUserNameFromToken(token));
        } catch (UserException e) {
            return new ResponseEntity<>(new AppError("HISTORY FAILED", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(transactions);

    }
}
