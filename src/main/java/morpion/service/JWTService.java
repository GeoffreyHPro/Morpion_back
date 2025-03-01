package morpion.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import morpion.model.User;

@Service
@SuppressWarnings("deprecation")
public class JWTService {
    final private SecretKey key;
    final private JwtParser parser;

    
    public JWTService() {
        this.key = Keys.hmacShaKeyFor("12386416848646146846414684165518474651.960846".getBytes());
        this.parser = Jwts.parser().setSigningKey(this.key).build();
    }

    public String generate(String userName) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
                .signWith(key);
        return builder.compact();
    }

    public String getUserName(String token) {
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public Boolean validate(User user, String token) {
        Claims claims = parser.parseClaimsJws(token).getBody();
        boolean unexpired = claims.getExpiration().before(Date.from(Instant.now()));
        return !unexpired && user.getEmail().equals(claims.getSubject());
    }
}
