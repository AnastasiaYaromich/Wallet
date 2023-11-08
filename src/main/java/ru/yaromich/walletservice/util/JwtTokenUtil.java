package ru.yaromich.walletservice.util;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.yaromich.walletservice.domain.models.Role;
import ru.yaromich.walletservice.domain.models.User;


import java.util.*;

@Component
public class JwtTokenUtil {

    private final Environment environment;

    @Autowired
    public JwtTokenUtil(Environment environment) {
        this.environment = environment;
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = user.getRoles().stream().map(Role::getName).toList();
        claims.put("roles", roles);

        Date issuedDate = new Date();
        Date expiredDate = addHoursToIssuedDate(issuedDate,
                Integer.parseInt(environment.getRequiredProperty("jwt.lifetime")));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getLogin())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, environment.getRequiredProperty("jwt.secret"))
                .compact();
    }

    public String getUserNameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(environment.getRequiredProperty("jwt.secret"))
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(environment.getRequiredProperty("jwt.secret")).parse(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException |
                 IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")) {
            return header.substring(header.indexOf("Bearer") + 7);
        }
        return null;
    }

    private Date addHoursToIssuedDate(Date issuedDate, int addedHours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issuedDate);
        calendar.add(Calendar.HOUR_OF_DAY, addedHours);
        return calendar.getTime();
    }




}
