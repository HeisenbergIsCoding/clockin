package com.clockin.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT服務類
 */
@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret:defaultSecretKeyWhichShouldBeLongEnoughToBeSecure}")
    private String secret;

    @Value("${jwt.expiration:86400}")
    private long expiration;

    @Value("${jwt.cookie.name:CLOCKIN_TOKEN}")
    private String cookieName;

    /**
     * 生成JWT令牌
     *
     * @param email  用戶郵箱
     * @param userId 用戶ID
     * @return JWT令牌
     */
    public String generateToken(String email, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return createToken(claims, email);
    }

    /**
     * 創建令牌
     *
     * @param claims 聲明
     * @param email  用戶郵箱
     * @return JWT令牌
     */
    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 獲取簽名密鑰
     *
     * @return 簽名密鑰
     */
    private javax.crypto.SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 從令牌中獲取用戶郵箱
     *
     * @param token 令牌
     * @return 用戶郵箱
     */
    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 從令牌中獲取用戶ID
     *
     * @param token 令牌
     * @return 用戶ID
     */
    public Long getUserIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("userId", Long.class));
    }

    /**
     * 從令牌中獲取過期時間
     *
     * @param token 令牌
     * @return 過期時間
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 從令牌中獲取聲明
     *
     * @param token          令牌
     * @param claimsResolver 聲明解析器
     * @param <T>            聲明類型
     * @return 聲明
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 從令牌中獲取所有聲明
     *
     * @param token 令牌
     * @return 所有聲明
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 驗證令牌是否過期
     *
     * @param token 令牌
     * @return 是否過期
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 驗證令牌是否有效
     *
     * @param token 令牌
     * @return 是否有效
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            log.error("驗證令牌失敗", e);
            return false;
        }
    }

    /**
     * 從請求中獲取令牌
     *
     * @param request 請求
     * @return 令牌
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        // 從Cookie中獲取令牌
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        
        // 從請求頭中獲取令牌
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        
        return null;
    }

    /**
     * 設置令牌Cookie
     *
     * @param response 響應
     * @param token    令牌
     */
    public void setCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) expiration);
        response.addCookie(cookie);
    }

    /**
     * 清除令牌Cookie
     *
     * @param response 響應
     */
    public void clearCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
