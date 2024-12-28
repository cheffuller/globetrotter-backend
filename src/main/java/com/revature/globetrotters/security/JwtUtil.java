package com.revature.globetrotters.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/*
    Do not use '-' in any subject or claims.
*/
public class JwtUtil {
    // Remove the secretKey in a prod environment and get it from an env file that isn't shared to GitHub
    private static final String secretKey = "r3hoQxC0q99pZ1dC36g7Ja0X3DZr6RqYPOXaoqz3xkc";
    private static final int tokenLifespanInSeconds = 3600; // Remove if expiration is unnecessary
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public static String generateTokenFromUserName(String username, Map<String, String> claims) {
        logger.info("Jwt received username: {}", username);

        String token = Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenLifespanInSeconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        logger.info("Jwt generated token: {}", token);
        return token;
    }

    public static String extractSubjectFromToken(String token) {
        logger.info("Jwt received token: {}.", token);

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        logger.info("Jwt extracted value: {}", claims.getSubject());
        return claims.getSubject();
    }

    public static Object extractValueFromTokenByKey(String token, String key) {
        logger.info("Jwt received token: {}.\nJwt received key: {}.", token, key);

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        logger.info("Jwt extracted value: {}", claims.get(key));
        return claims.get(key);
    }
}
