package morpion.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
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

    public Boolean validate(UserDetails user, String token) {
        Claims claims = parser.parseClaimsJws(token).getBody();
        boolean unexpired = claims.getExpiration().before(Date.from(Instant.now()));
        //System.out.println(claims.getSubject() + "  :  " + user.getUsername());
        return !unexpired && user.getUsername().equals(claims.getSubject());
    }
}
