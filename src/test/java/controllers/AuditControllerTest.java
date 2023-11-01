package controllers;

import com.yaromich.wallet.config.JwtRequestFilter;
import com.yaromich.wallet.infrastructure.in.controllers.AuditController;
import com.yaromich.wallet.logic.services.AuditServiceImpl;
import com.yaromich.wallet.logic.services.UserServiceImpl;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
class AuditControllerTest {

    private static AuditServiceImpl auditService;
    private static UserServiceImpl userService;
    private static JwtRequestFilter jwtRequestFilter;
    private static JwtTokenUtil jwtTokenUtil;
    private static ObjectMapper objectMapper;
    private static MockMvc mockMvc;

    @BeforeAll
    public static void setUp() {
        userService = Mockito.mock(UserServiceImpl.class);
        jwtTokenUtil = Mockito.mock(JwtTokenUtil.class);
        jwtRequestFilter = Mockito.mock(JwtRequestFilter.class);
        auditService = Mockito.mock(AuditServiceImpl.class);

        AuditController auditController = new AuditController(auditService, userService, jwtRequestFilter, jwtTokenUtil);
        mockMvc = MockMvcBuilders.standaloneSetup(auditController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldGetAuditByUsername() throws Exception {
        mockMvc.perform(
                get("/audit/Afina")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isUnauthorized());
    }
}
