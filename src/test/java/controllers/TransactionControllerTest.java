package controllers;

import com.yaromich.wallet.config.JwtRequestFilter;
import com.yaromich.wallet.domain.dto.TransactionRequest;
import com.yaromich.wallet.infrastructure.in.controllers.TransactionController;
import com.yaromich.wallet.logic.services.TransactionServiceImpl;
import com.yaromich.wallet.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
class TransactionControllerTest {
    private static TransactionServiceImpl transactionService;
    private static JwtTokenUtil jwtTokenUtil;
    private static JwtRequestFilter jwtRequestFilter;
    private static ObjectMapper objectMapper;
    private static HttpServletRequest request;
    private static MockMvc mockMvc;

    @BeforeAll
    public static void setUp() {
        transactionService = Mockito.mock(TransactionServiceImpl.class);
        jwtTokenUtil = Mockito.mock(JwtTokenUtil.class);
        jwtRequestFilter = Mockito.mock(JwtRequestFilter.class);
        request = Mockito.mock(HttpServletRequest.class);

        TransactionController transactionController = new TransactionController(transactionService, jwtTokenUtil, jwtRequestFilter);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldNotWithdrawMoney() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setUsername("The cat with the guitar");
        transactionRequest.setTransactionId("1");
        transactionRequest.setAmount(BigDecimal.valueOf(100));
        transactionRequest.setType("withdraw");

        mockMvc.perform(
            post("/transaction/withdraw")
                    .content(new ObjectMapper().writeValueAsBytes(transactionRequest))
                    .contentType(MediaType.APPLICATION_JSON)

                ).andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldNotReplenishMoney() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setUsername("The cat with the guitar");
        transactionRequest.setTransactionId("1");
        transactionRequest.setAmount(BigDecimal.valueOf(100));
        transactionRequest.setType("withdraw");

        mockMvc.perform(
                post("/transaction/replenish")
                        .content(new ObjectMapper().writeValueAsBytes(transactionRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldNotReturnHistory() throws Exception {
        mockMvc.perform(
                get("/transaction/history")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isUnauthorized());
    }
}
