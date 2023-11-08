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
import ru.yaromich.walletservice.infrastructure.in.controllers.AuditController;
import ru.yaromich.walletservice.logic.services.AuditServiceImpl;
import ru.yaromich.walletservice.logic.services.exceptions.UserException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class AuditControllerTest {
    private static AuditServiceImpl auditService;
    private static ObjectMapper objectMapper;
    private static MockMvc mockMvc;

    public static void setUp() {
        auditService = Mockito.mock(AuditServiceImpl.class);
        AuditController auditController = new AuditController(auditService);
        mockMvc = MockMvcBuilders.standaloneSetup(auditController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldReturnAuditByUsername() throws Exception {
        String name = "Test";

        setUp();
        mockMvc.perform(
                get("/audit/" + name)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void shouldNotReturnAuditByUsername() throws Exception {
        String name = "Test";

        setUp();
        Mockito.doThrow(new UserException("User not found")).when(auditService).findAllByUserName(name);
        mockMvc.perform(
                get("/audit/" + name)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isBadRequest());
    }
}
