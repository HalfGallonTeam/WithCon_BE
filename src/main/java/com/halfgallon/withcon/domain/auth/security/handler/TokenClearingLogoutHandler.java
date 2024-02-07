package com.halfgallon.withcon.domain.auth.security.handler;

import com.halfgallon.withcon.domain.auth.repository.AccessTokenRepository;
import com.halfgallon.withcon.domain.auth.repository.RefreshTokenRepository;
import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Slf4j
@RequiredArgsConstructor
public class TokenClearingLogoutHandler implements LogoutHandler {

  private final AccessTokenRepository accessTokenRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {

    log.info("로그아웃 중 : 저장된 토큰 삭제");

    CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
    Long memberId = principal.getId();

    accessTokenRepository.deleteById(memberId);
    refreshTokenRepository.deleteById(memberId);
  }
}
