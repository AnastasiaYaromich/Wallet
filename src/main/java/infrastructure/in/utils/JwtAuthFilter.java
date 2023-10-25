package infrastructure.in.utils;

import aop.annotations.Speed;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Speed
@WebFilter("/*")
public class JwtAuthFilter implements Filter {

    private final ObjectMapper objectMapper;
    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("/authorize", "/register")));

    public JwtAuthFilter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = JwtTokenUtil.extractToken(request);
        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("/+$", "");

        if((token != null && JwtTokenUtil.isValidToken(token) || ALLOWED_PATHS.contains(path))) {
            System.out.println("Check");
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.setStatus(HttpServletResponse.SC_OK);
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().write(this.objectMapper.writeValueAsBytes("You are not authorized!"));
            }
    }
}
