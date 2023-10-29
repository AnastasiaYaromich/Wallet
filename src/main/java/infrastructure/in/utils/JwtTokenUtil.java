package infrastructure.in.utils;

import java.util.Date;

import aop.annotations.Speed;
import jakarta.servlet.http.HttpServletRequest;
import io.jsonwebtoken.*;

@Speed
public class JwtTokenUtil{
    private static final String secret = "11h4f8093h4f983yhrt9834hr0934hf0hf493g493gf438rh438th34g34g";

    public String generateToken(String login) {
        Date issuedDate = new Date();
        Date expiredDate = new Date();
        expiredDate.setDate(issuedDate.getDate() + 7);
        return Jwts.builder()
                .setSubject(login)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

    }

    public static String parseToken(String token) {
        return  (Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject()).toString();
    }

    public static void changeToken(String token) {
        Date expiredDate = new Date();
        token = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .setExpiration(expiredDate).getSubject();
    }

    public static String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")) {
            return header.substring(header.indexOf("Bearer") + 7);
        }
        return null;
    }

    public static boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parse(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException |
                 IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }
}
