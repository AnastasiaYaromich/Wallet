package infrastructure.in.servlets;

import aop.annotations.Speed;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.AuditDto;
import dto.AuditInfoDto;
import infrastructure.in.utils.JwtTokenUtil;
import infrastructure.in.utils.ParseUtils;
import infrastructure.out.audit.AuditServiceSingleton;
import infrastructure.out.user.UserServiceSingleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.AuditService;
import services.UserService;
import services.exceptions.UserException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@Speed
public class AuditServlet extends HttpServlet {

    private final UserService userService;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public AuditServlet() throws SQLException {
        this.auditService = AuditServiceSingleton.getAuditService();
        this.userService = UserServiceSingleton.getUserService();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = JwtTokenUtil.extractToken(request);
        String requestBody = ParseUtils.getRequestBody(request.getInputStream());
        String login = JwtTokenUtil.parseToken(token);

        try {
            if(userService.findUserByLogin(login).getRole().equals("admin")) {
                AuditInfoDto infoDto = objectMapper.readValue(requestBody, AuditInfoDto.class);
                List<AuditDto> auditsDto = auditService.findAll(infoDto.getUsername());

                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                String message = this.objectMapper.writeValueAsString(auditsDto);
                response.getOutputStream().write(this.objectMapper.writeValueAsBytes(message));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getOutputStream().write(this.objectMapper.writeValueAsBytes("You doesn't have enough permissions to see the audit"));
            }
        } catch (UserException e) {
            e.printStackTrace();
        }
    }

}
