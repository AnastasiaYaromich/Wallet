package com.yaromich.wallet.utils;

import com.yaromich.wallet.config.YamlPropertySourceFactory;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
@PropertySource(value="classpath:application.yaml", factory = YamlPropertySourceFactory.class)
public class JwtTokenUtil  {

    private final Environment environment;

    @Autowired
    public JwtTokenUtil(Environment environment) {
        this.environment = environment;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public String generateToken(UserDetails userDetails)  {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        claims.put("roles", roles);

        Date issuedDate = new Date();
        Date expiredDate = addHoursToJavaUtilDate(issuedDate,
                Integer.parseInt(environment.getRequiredProperty("jwt.lifetime")));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
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

    private Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")) {
            return header.substring(header.indexOf("Bearer") + 7);
        }
        return null;
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
}
