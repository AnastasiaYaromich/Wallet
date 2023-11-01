package controllers;

import com.yaromich.wallet.domain.dto.JwtRequest;
import com.yaromich.wallet.infrastructure.in.controllers.AuthorizationController;
import com.yaromich.wallet.logic.services.UserServiceImpl;
import com.yaromich.wallet.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
class AuthorizationControllerTest {
    private static UserServiceImpl userService;
    private static JwtTokenUtil jwtTokenUtil;
    private static ObjectMapper objectMapper;
    private static MockMvc mockMvc;

    @BeforeAll
    public static void setUp() {
        userService = Mockito.mock(UserServiceImpl.class);
        jwtTokenUtil = Mockito.mock(JwtTokenUtil.class);

        AuthorizationController authController = new AuthorizationController(userService, jwtTokenUtil);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldCreateAuthToken() throws Exception {
        JwtRequest request = new JwtRequest();
        request.setLogin("John");
        request.setPassword("100");
        String token = "token";
        UserDetails userDetails = new User("John", "100", List.of());

        Mockito.when(userService.loadUserByUsername(request.getLogin())).thenReturn(userDetails);
        Mockito.when(jwtTokenUtil.generateToken(userDetails)).thenReturn(token);

        mockMvc.perform(
                post("/authorize")
                        .content(new ObjectMapper().writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk());
    }
}
