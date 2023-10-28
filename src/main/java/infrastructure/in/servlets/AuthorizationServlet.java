package infrastructure.in.servlets;

import aop.annotations.Speed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dto.UserDto;
import infrastructure.in.utils.JwtTokenUtil;
import infrastructure.in.utils.ParseUtils;
import infrastructure.out.user.UserServiceSingleton;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import services.UserService;
import services.exceptions.UserException;
import java.io.IOException;
import java.sql.SQLException;

@Speed
public class AuthorizationServlet extends HttpServlet {

    private final ObjectMapper objectMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public AuthorizationServlet() throws SQLException {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.jwtTokenUtil = new JwtTokenUtil();
        this.userService = UserServiceSingleton.getUserService();
    }

    public AuthorizationServlet(UserService userService, ObjectMapper objectMapper, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String authHeader = request.getHeader("authorization");

        if(authHeader != null && checkIfTokenIsValid(authHeader)) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            sendResponse(response, "User already logged in");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            sendResponse(response, "Invalid token");
        }
        UserDto userDto = getUserCredentialsIfIsExist(request);

        try {
            if(userDto != null && checkIfCredentialsIsValid(userDto)) {
                String token = jwtTokenUtil.generateToken(userDto.getLogin());
                response.setStatus(HttpServletResponse.SC_OK);
                sendResponse(response, token);
            }
        } catch (UserException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            sendResponse(response, "Bad credentials. Incorrect login or password");
        }
    }

    private boolean checkIfCredentialsIsValid(UserDto userDto) throws UserException {
        UserDto user = userService.findUserByLogin(userDto.getLogin());
        return user != null && user.getPassword().equals(userDto.getPassword());
    }

    private UserDto getUserCredentialsIfIsExist(HttpServletRequest request) {
        String login = (String) request.getAttribute("login");
        String password = (String) request.getAttribute("password");
        if(login != null && password != null) {
            UserDto userDto = new UserDto();
            userDto.setLogin(login);
            userDto.setPassword(password);
            return userDto;
        }
        return null;
    }

    private boolean checkIfTokenIsValid(String authHeader) {
        String token = authHeader.substring(authHeader.indexOf("Bearer") + 7);
        return JwtTokenUtil.isValidToken(token);
    }

    private void sendResponse(HttpServletResponse response, String message) throws IOException {
        response.getStatus();
        response.setContentType("application/json");
        response.getOutputStream().write(this.objectMapper.writeValueAsBytes(message));
    }
}






