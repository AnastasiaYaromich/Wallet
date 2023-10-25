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

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String authHeader = request.getHeader("authorization");
        try {
            String requestBody = ParseUtils.getRequestBody(request.getInputStream());

            UserDto userDto = null;
            if (!requestBody.isEmpty()) {
                userDto = objectMapper.readValue(requestBody, UserDto.class);
            }

            if (authHeader != null) {
                String accessToken = authHeader.substring(authHeader.indexOf("Bearer") + 7);
                if (JwtTokenUtil.isValidToken(accessToken)) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    sendResponse(response, "User already logged in");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    sendResponse(response, "Invalid token");
                }
            } else if (userDto != null) {
                String token;
                try {
                    if (userService.findUserByLogin(userDto.getLogin()).getPassword().equals(userDto.getPassword())) {
                        token = jwtTokenUtil.generateToken(userDto.getLogin());
                        response.setStatus(HttpServletResponse.SC_OK);
                        sendResponse(response, token);
                    } else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        sendResponse(response, "Bad credentials. Incorrect login or password");
                    }
                } catch (UserException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    sendResponse(response, "Bad credentials. Incorrect login or password");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(HttpServletResponse response, String message) throws IOException {
        response.getStatus();
        response.setContentType("application/json");
        response.getOutputStream().write(this.objectMapper.writeValueAsBytes(message));
    }
}






