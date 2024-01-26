package com.halfgallon.withcon.domain.auth.security.handler;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

  private static final String ACCESS_TOKEN_HEADER_NAME = "Authorization";
  private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    log.info("로그인 성공");

    // 토큰 생성
    String accessToken = "임시 액세스 토큰";
    String refreshToken = "임시 리프래시 토큰";

    // 리프래시 토큰 쿠키 생성
    Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(true);
    // TODO: 쿠키의 MaxAge 설정을 refresh token 의 만료시간과 동일하게 줄 것인가? 에 대한 고민.
    // 디폴트 값은 브라우저가 닫힐 시 쿠키도 삭제됨

    // 응답
    response.setHeader(ACCESS_TOKEN_HEADER_NAME, accessToken);
    response.addCookie(refreshTokenCookie);
    response.setContentType(APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(UTF_8.name());
    response.setStatus(OK.value());
  }
}
