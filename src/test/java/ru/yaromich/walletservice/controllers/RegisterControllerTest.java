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
import ru.yaromich.walletservice.domain.dtos.RegisterDto;
import ru.yaromich.walletservice.infrastructure.in.controllers.RegisterController;
import ru.yaromich.walletservice.logic.services.UserServiceImpl;
import ru.yaromich.walletservice.logic.services.exceptions.UserException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class RegisterControllerTest {
    private static UserServiceImpl userService;
    private static ObjectMapper objectMapper;
    private static MockMvc mockMvc;

    public static void setUp() {
        userService = Mockito.mock(UserServiceImpl.class);
        RegisterController registerController = new RegisterController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(registerController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldRegisterSuccessfully() throws Exception {
        setUp();
        RegisterDto registerDto = new RegisterDto();
        registerDto.setLogin("Test");
        registerDto.setPassword("Test");

        mockMvc.perform(
                post("/register")
                        .content(new ObjectMapper().writeValueAsBytes(registerDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void shouldRegisterFailed() throws Exception {
        setUp();
        RegisterDto registerDto = new RegisterDto();
        registerDto.setLogin("Test");
        registerDto.setPassword("Test");

        Mockito.doThrow(new UserException("User already exist")).when(userService).save(registerDto);

        mockMvc.perform(
                post("/register")
                        .content(new ObjectMapper().writeValueAsBytes(registerDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isBadRequest());
    }
}
