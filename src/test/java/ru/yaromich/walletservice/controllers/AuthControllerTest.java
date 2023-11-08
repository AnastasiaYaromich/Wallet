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
import ru.yaromich.walletservice.domain.dtos.JwtRequest;
import ru.yaromich.walletservice.infrastructure.in.controllers.AuthController;
import ru.yaromich.walletservice.logic.services.UserServiceImpl;
import ru.yaromich.walletservice.logic.services.exceptions.UserException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class AuthControllerTest {
    private static UserServiceImpl userService;
    private static ObjectMapper objectMapper;
    private static MockMvc mockMvc;

    public static void setUp() {
        userService = Mockito.mock(UserServiceImpl.class);
        AuthController authController = new AuthController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldAuthorizeSuccessfully() throws Exception {
        setUp();
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setLogin("The cat with the guitar");
        jwtRequest.setPassword("BestCat");
        String token = "someToken";

        Mockito.when(userService.authenticate(jwtRequest)).thenReturn(token);

        mockMvc.perform(
                post("/authorize")
                        .content(new ObjectMapper().writeValueAsBytes(jwtRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void shouldAuthorizeFailed() throws Exception {
        setUp();
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setLogin("The cat with the guitar");
        jwtRequest.setPassword("BestCat");

        Mockito.when(userService.authenticate(jwtRequest)).thenThrow(UserException.class);

        mockMvc.perform(
                post("/authorize")
                        .content(new ObjectMapper().writeValueAsBytes(jwtRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isUnauthorized());
    }
}


