package com.codingchallenge.spotify.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.codingchallenge.spotify.security.SecurityConstants.MOCK_USERNAME;

@Service
public class JwtTokenProvider {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    private String secret = "mySecretKey";

    public String getUsernameFromToken(String token) {
        return token == null || token.isEmpty() || token.isBlank() ? "" : getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret) // This is valid in 0.9.0
                .parseClaimsJws(token) // Parse claims using the secret
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userName);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder() // This is the old builder style, supported in 0.9.0
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256, secret) // Using HS256, which is valid in 0.9.0
                .compact();
    }

    public Boolean validateToken(String token) {
        final String userName = getUsernameFromToken(token);
        return (userName != null && !userName.isEmpty() && MOCK_USERNAME.equalsIgnoreCase(userName) && !isTokenExpired(token));
    }
}
