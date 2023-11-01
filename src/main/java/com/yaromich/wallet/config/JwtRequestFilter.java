package com.yaromich.wallet.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaromich.wallet.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
public class JwtRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtRequestFilter(ObjectMapper objectMapper, JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }



    public boolean filter(HttpServletRequest request) throws IOException, ServletException {
        String token = jwtTokenUtil.extractToken(request);

        if(token != null && jwtTokenUtil.isValidToken(token)) {
            return true;
        } else {
            return false;
        }
    }


}
