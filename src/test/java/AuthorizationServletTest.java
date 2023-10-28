import com.fasterxml.jackson.databind.ObjectMapper;
import dto.UserDto;
import infrastructure.in.servlets.AuthorizationServlet;
import infrastructure.in.utils.JwtTokenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import services.UserService;

import java.io.IOException;

public class AuthorizationServletTest {

    private AuthorizationServlet authorizationServlet;
    private UserService userService;
    private ObjectMapper objectMapper;
    private HttpServletResponse response;
    private HttpServletRequest request;
    private ServletOutputStream servletOutputStream;
    private UserDto userDto;
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setUp() {
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        servletOutputStream = Mockito.mock(ServletOutputStream.class);
        objectMapper = new ObjectMapper();
        userService = Mockito.mock(UserService.class);
        jwtTokenUtil = new JwtTokenUtil();
        authorizationServlet = new AuthorizationServlet(userService, objectMapper, jwtTokenUtil);
        userDto = new UserDto();
        userDto.setLogin("John");
        userDto.setPassword("100");
    }

    @Test
    void shouldReturnInvalidTokenResponse() throws ServletException, IOException {
        String authHeader = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE2OTg0OTgyMDIsImV4cCI6MTczMDAzNDIwMiwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAZXhhbXBsZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWluaXN0cmF0b3IiXX0.4PZLizBN4MMACSRO1xATdlrdg6DFTWN6xdUGy6L9lI";
        String token = authHeader.substring(authHeader.indexOf("Bearer") + 7);

        Mockito.when(request.getHeader("authorization")).thenReturn(token);
        Mockito.when(response.getOutputStream()).thenReturn(servletOutputStream);
        authorizationServlet.doGet(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response).setContentType("application/json");
    }

    @Test
    void shouldReturnBadCredentialsResponse() throws IOException, ServletException {
        request.setAttribute("login", userDto.getLogin());
        request.setAttribute("password", userDto.getPassword());

        Mockito.when(request.getAttribute("login")).thenReturn(userDto.getLogin());
        Mockito.when(request.getAttribute("password")).thenReturn(userDto.getPassword());
        Mockito.when(response.getOutputStream()).thenReturn(servletOutputStream);
        authorizationServlet.doGet(request, response);

        Mockito.verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Mockito.verify(response).setContentType("application/json");
    }
}
