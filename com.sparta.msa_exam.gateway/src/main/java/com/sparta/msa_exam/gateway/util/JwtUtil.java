package com.sparta.msa_exam.gateway.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Component
public class JwtUtil {

    public static final Logger logger = LoggerFactory.getLogger("Gateway: JWT 관련 로그");

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logger.error("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty");
        }
        return false;
    }
}
