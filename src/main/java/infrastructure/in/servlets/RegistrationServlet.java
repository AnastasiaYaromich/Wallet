package infrastructure.in.servlets;

import aop.annotations.Speed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dto.UserDto;
import infrastructure.in.utils.JwtTokenUtil;
import infrastructure.in.utils.ParseUtils;
import infrastructure.out.user.UserServiceSingleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.UserService;
import services.exceptions.UserException;
import services.exceptions.ValidationException;
import java.io.IOException;
import java.sql.SQLException;

@Speed
public class RegistrationServlet extends HttpServlet {

    private final ObjectMapper objectMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public RegistrationServlet() throws SQLException {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.jwtTokenUtil = new JwtTokenUtil();
        this.userService = UserServiceSingleton.getUserService();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto userDto = null;
        String token;

        try {
            String requestBody = ParseUtils.getRequestBody(request.getInputStream());
            if (!requestBody.isEmpty()) {
                userDto = objectMapper.readValue(requestBody, UserDto.class);

                userService.findUserByLogin(userDto.getLogin());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                sendResponse(response, "User with these credentials already exist");
            }
        } catch (UserException e) {
            try {
                userService.save(userDto);
                token = jwtTokenUtil.generateToken(userDto.getLogin());
                response.setStatus(HttpServletResponse.SC_CREATED);
                sendResponse(response, token);
            } catch (IllegalArgumentException | IOException | ValidationException ex) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                sendResponse(response, ex.getMessage());
            }
        }
    }

    private void sendResponse(HttpServletResponse response, String message) throws IOException {
        response.getStatus();
        response.setContentType("application/json");
        response.getOutputStream().write(this.objectMapper.writeValueAsBytes(message));
    }
}
