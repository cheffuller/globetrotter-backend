package com.revature.globetrotters.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class JwtUtil {
    private static final String secretKey = "jwt-secret-key";
    private static final int tokenLifespanInSeconds = 3600; // Remove if expiration is unnecessary
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public static String generateToken(String username) {
        logger.info("Jwt received username: {}", username);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenLifespanInSeconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        logger.info("Jwt generated token: {}", token);
        return token;
    }

    public static String extractUsername(String token) {
        logger.info("Jwt received token: {}", token);

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        logger.info("Jwt extracted username: {}", claims.getSubject());
        return claims.getSubject();
    }
}
