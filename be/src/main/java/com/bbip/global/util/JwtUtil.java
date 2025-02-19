package com.bbip.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    private final String SECRET = "bbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbipbbip";
    private final long JWT_EXPIRATION = 3600000;  // Access Token 유효 기간, 단위:ms (1시간)
    private final long REFRESH_TOKEN_EXPIRATION = 86400;  // Refresh Token 유효 기간, 단위:s (1일)

    private final String JWT_SECRET = Base64.getEncoder().encodeToString(SECRET.getBytes());
    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    // Access Token 생성
    public String generateToken(String email, Integer userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        return TOKEN_PREFIX + Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    // Refresh Token 생성
    public String generateRefreshToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION*1000);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    // Refresh Token 만료 시간 가져오기
    public long getRefreshTokenExpirationTime() {
        return REFRESH_TOKEN_EXPIRATION;
    }

    // JWT 토큰에서 사용자 이름 추출
    public String getEmailFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(resolveToken(token))
                .getBody();

        return claims.getSubject();
    }

    // JWT 토큰에서 사용자 아이디 추출
    public Integer getUserIdFromJWT(String tokenWithBearer) {
        return 1;
//        log.info("tokenWithBearer: {}", tokenWithBearer);
//
//        Claims claims = Jwts.parser()
//                .setSigningKey(JWT_SECRET)
//                .parseClaimsJws(resolveToken(tokenWithBearer))
//                .getBody();
//        log.info("claims: {}", claims);
//
//        log.info("userId: {}", claims.get("userId"));
//
//        return claims.get("userId", Integer.class);
    }

    // JWT 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 요청 헤더에서 JWT를 추출하는 메서드
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_STRING);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);  // "Bearer " 이후의 토큰 부분만 추출
        }
        return null;
    }

    public String resolveToken(String token) {
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(7); // "Bearer " 제거
        }
        log.info("token: {}", token);
        return token;
    }
}
