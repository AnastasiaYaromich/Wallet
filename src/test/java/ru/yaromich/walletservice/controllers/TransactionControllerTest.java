package ru.yaromich.walletservice.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.yaromich.walletservice.domain.dtos.TransactionRequest;
import ru.yaromich.walletservice.infrastructure.in.controllers.TransactionController;
import ru.yaromich.walletservice.logic.services.TransactionServiceImpl;
import ru.yaromich.walletservice.logic.services.exceptions.TransactionException;
import ru.yaromich.walletservice.services.TransactionService;
import ru.yaromich.walletservice.util.JwtTokenUtil;
import java.math.BigDecimal;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class TransactionControllerTest {
    private static TransactionService transactionService;
    private static JwtTokenUtil jwtTokenUtil;
    private static ObjectMapper objectMapper;
    private static MockMvc mockMvc;
    private static TransactionRequest transactionRequest;

    public static void setUp() {
        transactionService = Mockito.mock(TransactionServiceImpl.class);
        jwtTokenUtil = Mockito.mock(JwtTokenUtil.class);
        TransactionController transactionController = new TransactionController(transactionService, jwtTokenUtil);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        objectMapper = new ObjectMapper();
        transactionRequest = new TransactionRequest();
        transactionRequest.setTransactionId("1");
        transactionRequest.setType("withdraw");
        transactionRequest.setAmount(BigDecimal.valueOf(100));
        transactionRequest.setUsername("Test");
    }

    @Test
    public void shouldWithdrawMoneySuccessfully() throws Exception {
        setUp();
        mockMvc.perform(
                post("/transaction/withdraw")
                        .content(new ObjectMapper().writeValueAsBytes(transactionRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void shouldWithdrawMoneyFailedTransactionIdIsNotUnique() throws Exception {
        setUp();
        doThrow(new TransactionException("Transaction id is not unique")).when(transactionService).withdraw(transactionRequest);
        mockMvc.perform(
                post("/transaction/withdraw")
                        .content(new ObjectMapper().writeValueAsBytes(transactionRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldWithdrawMoneyFailedNotEnoughMoneyInWallet() throws Exception {
        setUp();
        doThrow(new TransactionException("Not enough money in your wallet")).when(transactionService).withdraw(transactionRequest);
        mockMvc.perform(
                post("/transaction/withdraw")
                        .content(new ObjectMapper().writeValueAsBytes(transactionRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReplenishMoneySuccessfully() throws Exception {
        setUp();
        mockMvc.perform(
                post("/transaction/replenish")
                        .content(new ObjectMapper().writeValueAsBytes(transactionRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void shouldReplenishMoneyFailedTransactionIdIsNotUnique() throws Exception {
        setUp();
        doThrow(new TransactionException("Transaction id is not unique")).when(transactionService).replenish(transactionRequest);
        mockMvc.perform(
                post("/transaction/replenish")
                        .content(new ObjectMapper().writeValueAsBytes(transactionRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetHistorySuccessful() throws Exception {
        setUp();
        mockMvc.perform(
                get("/transaction/history")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk());
    }
}

