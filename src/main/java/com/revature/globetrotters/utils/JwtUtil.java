package com.revature.globetrotters.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/*
    Do not use '-' in any subject or claims.
*/
public class JwtUtil {
    private static final int tokenLifespanInSeconds = 3600; // Remove if expiration is unnecessary
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    // Remove the secretKey in a prod environment and get it from an env file that isn't shared to GitHub
    private static final String secret = "r3hoQxC0q99pZ1dC36g7Ja0X3DZr6RqYPOXaoqz3xkc";
    private static final SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));


    public static String generateTokenFromUserName(String username, Map<String, String> claims) {
        logger.info("Jwtutil received username: {}", username);
        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(tokenLifespanInSeconds)))
                .signWith(secretKey)
                .compact();
    }

    public static String extractSubjectFromToken(String token) {
        logger.info("Jwtutil received token: {}.", token);
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        logger.info("JwtUtil extracted value: {}", claims.getSubject());
        return claims.getSubject();
    }

    public static Object extractValueFromTokenByKey(String token, String key) {
        logger.info("JwtUtil received token: {}.\nJwt received key: {}.", token, key);
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        logger.info("JwtUtil extracted value: {}", claims.get(key));
        return claims.get(key);
    }
}
