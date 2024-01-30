package com.halfgallon.withcon.domain.auth.manager;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class JwtManager {

  @Value("${jwt.key}")
  private String key;

  private SecretKey secretKey;

  private static final long ACCESS_TOKEN_EXPIRY_MILLIS = 1000 * 60 * 30; // 30분
  private static final long REFRESH_TOKEN_EXPIRY_MILLIS = 1000 * 60 * 60 * 24 * 14; // 2주

  @PostConstruct
  private void initSecretKey() {
    secretKey = Keys.hmacShaKeyFor(key.getBytes());
  }

  public String createAccessToken(Long memberId) {
    return createToken(ACCESS_TOKEN_EXPIRY_MILLIS, memberId);
  }

  public String createRefreshToken(Long memberId) {
    return createToken(REFRESH_TOKEN_EXPIRY_MILLIS, memberId);
  }

  private String createToken(long accessTokenExpiryMillis, Long memberId) {
    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + accessTokenExpiryMillis);

    return Jwts.builder()
        .signWith(secretKey, SIG.HS256)
        .subject(String.valueOf(memberId))
        .expiration(expiredDate)
        .issuedAt(now)
        .compact();
  }
}
