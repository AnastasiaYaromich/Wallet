import com.fasterxml.jackson.databind.ObjectMapper;
import dto.UserDto;
import infrastructure.in.servlets.AuthorizationServlet;
import infrastructure.in.servlets.RegistrationServlet;
import infrastructure.in.utils.JwtTokenUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import services.UserService;
import services.exceptions.UserException;
import services.exceptions.ValidationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegistrationServletTest {
    private RegistrationServlet registrationServlet;
    private UserService userService;
    private ObjectMapper objectMapper;
    private HttpServletResponse response;
    private HttpServletRequest request;
    private ServletOutputStream servletOutputStream;
    private UserDto userDto;
    private JwtTokenUtil jwtTokenUtil;
    private BufferedReader bufferedReader;

    @BeforeEach
    public void setUp() {
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        servletOutputStream = Mockito.mock(ServletOutputStream.class);
        objectMapper = new ObjectMapper();
        userService = Mockito.mock(UserService.class);
        jwtTokenUtil = new JwtTokenUtil();
        registrationServlet = new RegistrationServlet(userService, objectMapper, jwtTokenUtil);
        userDto = new UserDto();
        userDto.setLogin("John");
        userDto.setPassword("100");
        bufferedReader = Mockito.mock(BufferedReader.class);
    }

    @Test
    void shouldRegisterUser() throws IOException, ServletException, ValidationException, UserException {
        String user = objectMapper.writeValueAsString(userDto);

        Mockito.when(request.getReader()).thenReturn(bufferedReader);
        Mockito.when(bufferedReader.ready()).thenReturn(true).thenReturn(false);
        Mockito.when(bufferedReader.readLine()).thenReturn(user);
        Mockito.when(response.getOutputStream()).thenReturn(servletOutputStream);

        registrationServlet.doPost(request, response);

//        Mockito.verify(userService).findUserByLogin(userDto.getLogin());
//        Mockito.verify(userService).save(userDto);
        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
        Mockito.verify(response).setContentType("application/json");
    }

}
