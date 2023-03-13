package com.kupreychik.parcellapp.security;

import com.kupreychik.parcellapp.command.AuthCommand;
import com.kupreychik.parcellapp.exception.UiError;
import com.kupreychik.parcellapp.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.kupreychik.parcellapp.exception.ParcelExceptionUtils.createParcelException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final String INVALID_JWT = "Invalid JWT";

    @Value("${jwt.expiration.time}")
    private Long expiration;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(AuthCommand authCommand, User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("user_id", user.getId());
        tokenData.put("role", user.getRole().getAuthority());
        tokenData.put("username", authCommand.getUsername());

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setClaims(tokenData)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        if (claims.getExpiration().before(new Date())) {
            throw new CredentialsExpiredException("JWT token expired ");
        }
        return String.valueOf(claims.get("username"));
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            log.error(INVALID_JWT, e);
            throw createParcelException(UiError.INVALID_JWT);
        }
    }
}
