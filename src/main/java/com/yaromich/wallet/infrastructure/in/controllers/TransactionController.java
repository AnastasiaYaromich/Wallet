package com.yaromich.wallet.infrastructure.in.controllers;

import com.yaromich.wallet.config.JwtRequestFilter;
import com.yaromich.wallet.domain.dto.TransactionDto;
import com.yaromich.wallet.domain.dto.TransactionRequest;
import com.yaromich.wallet.domain.dto.TransactionResponse;
import com.yaromich.wallet.logic.exceptions.AppError;
import com.yaromich.wallet.logic.exceptions.TransactionException;
import com.yaromich.wallet.logic.exceptions.UserException;
import com.yaromich.wallet.logic.services.TransactionServiceImpl;
import com.yaromich.wallet.utils.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Api("Методы для выполнения транзакций и просмотра истории")
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionServiceImpl transactionService;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public TransactionController(TransactionServiceImpl transactionService, JwtTokenUtil jwtTokenUtil, JwtRequestFilter jwtRequestFilter) {
        this.transactionService = transactionService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtRequestFilter = jwtRequestFilter;
    }


    @ApiOperation("Запрос на снятие денежных средств")
    @ApiResponses({
            @ApiResponse(code = 401, response = AppError.class, message = "Пользователь не авторизован в системе. " +
                    " Выполнение транзакций не разрешено"),
            @ApiResponse(code = 400, response = AppError.class, message = "Ошибка при попытке снятия денежных средств"),
            @ApiResponse(code = 200, response = TransactionResponse.class, message = "Снятие денежных средств произошло успешно")
    })
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody TransactionRequest transactionRequest, HttpServletRequest request) throws ServletException, IOException {
        if(jwtRequestFilter.filter(request)) {
            try {
                transactionService.withdraw(transactionRequest);
            } catch (UserException | TransactionException e) {

                return new ResponseEntity<>(new AppError("TRANSACTION_FAILED", e.getMessage()), HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.ok(new TransactionResponse("WITHDRAWAL WAS SUCCESSFUL", HttpStatus.OK));
        }
        return new ResponseEntity<>(new AppError("NOT ALLOWED", "UNAUTHORIZED"), HttpStatus.UNAUTHORIZED);
    }


    @ApiOperation("Запрос на пополнение счета")
    @ApiResponses({
            @ApiResponse(code = 401, response = AppError.class, message = "Пользователь не авторизован в системе. " +
                    " Выполнение транзакций не разрешено"),
            @ApiResponse(code = 400, response = AppError.class, message = "Ошибка при попытке пополнения счета"),
            @ApiResponse(code = 200, response = TransactionResponse.class, message = "Счет пополнен")
    })
    @PostMapping("/replenish")
    public ResponseEntity<?> replenish(@RequestBody TransactionRequest transactionRequest, HttpServletRequest request) throws ServletException, IOException {
        if(jwtRequestFilter.filter(request)) {
            try {
                transactionService.replenish(transactionRequest);
            } catch (UserException | TransactionException e) {

                return new ResponseEntity<>(new AppError("TRANSACTION FAILED", e.getMessage()), HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.ok(new TransactionResponse("REPLENISH WAS SUCCESSFUL", HttpStatus.OK));
        }
        return new ResponseEntity<>(new AppError("NOT ALLOWED", "UNAUTHORIZED"), HttpStatus.UNAUTHORIZED);
    }


    @ApiOperation("Запрос на просмотр истории операций")
    @ApiResponses({
            @ApiResponse(code = 401, response = AppError.class, message = "Пользователь не авторизован в системе. " +
                    " Просмотр истории невозможен"),
            @ApiResponse(code = 400, response = AppError.class, message = "Что-то пошло не так. Пользователь не найден"),
            @ApiResponse(code = 200, response = Map.class, message = "История транзакций найдена")
    })
    @GetMapping("/history")
    public ResponseEntity<?> history(HttpServletRequest request) throws ServletException, IOException {
        if(jwtRequestFilter.filter(request)) {
            String login = jwtTokenUtil.getUserNameFromToken(request.getHeader("authorization").substring(7));
            Map<String, List<TransactionDto>> history;
            try {
                history = transactionService.findAllByUserLogin(login);
                return ResponseEntity.ok(history);
            } catch (UserException e) {
                return new ResponseEntity<>(new AppError("HISTORY REQUEST FAILED", e.getMessage()), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(new AppError("NOT ALLOWED", "UNAUTHORIZED"), HttpStatus.UNAUTHORIZED);
    }
}
