package controllers;

import com.yaromich.wallet.domain.dto.RegisterUserDto;
import com.yaromich.wallet.infrastructure.in.controllers.RegistrationController;
import com.yaromich.wallet.logic.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
class RegistrationControllerTest {
    private static UserServiceImpl userService;
    private static MockMvc mockMvc;
    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void setUp() {
        userService = Mockito.mock(UserServiceImpl.class);
        RegistrationController registerController = new RegistrationController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(registerController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setLogin("The cat with the guitar");
        registerUserDto.setPassword("12345");
        registerUserDto.setRole("ROLE_USER");

        mockMvc.perform(
                post("/register")
                        .content(new ObjectMapper().writeValueAsBytes(registerUserDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk());
    }
}
