package com.halfgallon.withcon.domain.auth.security.handler;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.halfgallon.withcon.domain.auth.entity.AccessToken;
import com.halfgallon.withcon.domain.auth.entity.RefreshToken;
import com.halfgallon.withcon.domain.auth.manager.JwtManager;
import com.halfgallon.withcon.domain.auth.repository.AccessTokenRepository;
import com.halfgallon.withcon.domain.auth.repository.RefreshTokenRepository;
import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtManager jwtManager;
  private final AccessTokenRepository accessTokenRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  private static final String ACCESS_TOKEN_HEADER_NAME = "Authorization";
  private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
  private static final int REFRESH_TOKEN_COOKIE_EXPIRY = 60 * 60 * 24 * 14;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    log.info("로그인 성공");

    CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
    Long memberId = principal.getId();

    // 토큰 생성
    String accessToken = jwtManager.createAccessToken(memberId);
    String refreshToken = jwtManager.createRefreshToken(memberId);

    // 레디스에 토큰 저장
    accessTokenRepository.save(new AccessToken(memberId, accessToken));
    refreshTokenRepository.save(new RefreshToken(memberId, refreshToken));

    // 리프래시 토큰 쿠키 생성
    Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(true);
    refreshTokenCookie.setMaxAge(REFRESH_TOKEN_COOKIE_EXPIRY);

    // 응답
    response.setHeader(ACCESS_TOKEN_HEADER_NAME, accessToken);
    response.addCookie(refreshTokenCookie);
    response.setContentType(APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(UTF_8.name());
    response.setStatus(OK.value());
  }
}
