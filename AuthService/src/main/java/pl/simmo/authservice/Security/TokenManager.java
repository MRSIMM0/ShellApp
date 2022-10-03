package pl.simmo.authservice.Security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.simmo.authservice.DTO.LoginDTO;
import pl.simmo.authservice.Models.UserModel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class TokenManager {
    @Value("${simmo.jwt.valid}")
    private int validity;
    @Value("${simmo.jwt.secret}")
    private String jwtSecret;

    public String generateJwtToken(LoginDTO LoginDTO) {
        return Jwts.builder()
                .setSubject((LoginDTO.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + validity))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public Jws validateToken(String authToken) throws
            SignatureException,
            MalformedJwtException,
            ExpiredJwtException,
            UnsupportedJwtException,
            IllegalArgumentException {


        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
    }
}





