package pl.edu.agh.airsystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import pl.edu.agh.airsystem.security.model.JWTToken;

import java.util.Date;
import java.util.function.Function;


@Component
public class JWTTokenUtil {

    @Value("${jwt.access_token_expiration_time}")
    private long jwtTokenExpirationTime;

    @Value("${jwt.secret}")
    private String secret;


    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public JWTToken generateAccessToken(UserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        Date expirationDate = new Date(System.currentTimeMillis() + jwtTokenExpirationTime * 1000);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return new JWTToken(token, expirationDate);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}