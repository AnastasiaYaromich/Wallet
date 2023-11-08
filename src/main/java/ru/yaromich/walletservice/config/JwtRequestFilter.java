package ru.yaromich.walletservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.yaromich.walletservice.util.JwtTokenUtil;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private static final Set<String> ALLOWED_PATHS = Set.of("/authorize", "/register", "/swagger-ui/index.html", "/v3/**");

    @Autowired
    public JwtRequestFilter(ObjectMapper objectMapper, JwtTokenUtil jwtTokenUtil) {
        this.objectMapper = objectMapper;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenUtil.extractToken(request);
        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("/+$", "");

        if(path.startsWith("/v3") || path.startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response);
        }

        if(token != null && jwtTokenUtil.isValidToken(token) || ALLOWED_PATHS.contains(path)) {

            if(path.startsWith("/audit")) {
                List<String> roles = jwtTokenUtil.getRolesFromToken(token);
                for (String role: roles) {
                    if(role.equals("ROLE_ADMIN")) {
                        filterChain.doFilter(request, response);
                    }
                }
            }
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().write(objectMapper.writeValueAsBytes("You are not authorized"));
        }
    }


}
