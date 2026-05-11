package com.BookTracker.back.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import java.util.function.Function;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value; // <-- Обязательно этот!

@Service
public class JwtService {

    // Секретный ключ (в реальном проекте храните его в application.properties!)
    // Он должен быть длинным (не менее 256 бит для HMAC-SHA256)
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    // Генерация токена для пользователя
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(userDetails.getUsername()) // Обычно тут email
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Годен 1 день
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Извлечение email из токена
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Проверка, валиден ли токен
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey()) // заменили setSigningKey на verifyWith
                .build()
                .parseSignedClaims(token) // заменили parseClaimsJws на parseSignedClaims
                .getPayload(); // заменили getBody на getPayload
    }


    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}