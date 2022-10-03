package pl.simmo.authservice.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.simmo.authservice.Models.UserModel;

import java.time.Instant;
import java.util.Date;

@Component
public class VerificationManager {

    @Value("${simmo.verification.valid}")
    private int valid;
    @Value("${simmo.verification.secret}")
    private String secret;


    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
    public String generateToken(UserModel userModel){

      return   Jwts.builder()
                .setExpiration(new Date((new Date()).getTime() + valid))
                .setIssuedAt(new Date())
                .setSubject(userModel.getUsername())
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

}
