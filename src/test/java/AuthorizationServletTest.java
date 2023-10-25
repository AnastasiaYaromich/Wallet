import com.fasterxml.jackson.databind.ObjectMapper;
import domain.models.User;
import dto.UserDto;
import infrastructure.in.servlets.AuthorizationServlet;
import infrastructure.in.utils.JwtTokenUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import services.UserService;
import services.exceptions.UserException;

import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthorizationServletTest {

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
    public void doPost() throws SQLException, ServletException, UserException {
        String header = "authorization";

        when(header != null
                && JwtTokenUtil.isValidToken("sometoken.fghfgh.fghfgh")).thenReturn(true);
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        AuthorizationServlet servlet = new AuthorizationServlet();
        servlet.doPost(request, response);

//        UserDto user = new UserDto();
//        user.setLogin("Afina");
//        user.setPassword("12345");
//        when(userService.findUserByLogin(user.getLogin())).thenReturn(user);
//        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
//        AuthorizationServlet servlet = new AuthorizationServlet();
//        servlet.doPost(request, response);
//        verify(request).setAttribute("user", user);
    }
}
