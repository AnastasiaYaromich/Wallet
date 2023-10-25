import com.fasterxml.jackson.databind.ObjectMapper;
import dto.UserDto;
import infrastructure.in.servlets.AuthorizationServlet;
import infrastructure.in.servlets.RegistrationServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.mockito.Mock;
import services.UserService;
import services.exceptions.UserException;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RegistrationServletTest {

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpServletRequest request;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    UserService userService;

    @Test
    public void doPost() throws SQLException, ServletException, UserException, IOException {
        UserDto user = new UserDto();
        user.setLogin("Afina");
        user.setPassword("12345");
        when(userService.findUserByLogin(user.getLogin())).thenReturn(user);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        RegistrationServlet servlet = new RegistrationServlet();
        servlet.doPost(request, response);
        verify(request).setAttribute("user", user);
    }


}
