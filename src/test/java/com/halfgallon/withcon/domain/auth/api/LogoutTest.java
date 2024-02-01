package com.halfgallon.withcon.domain.auth.api;

import static com.halfgallon.withcon.domain.auth.constant.AuthConstant.REFRESH_TOKEN_COOKIE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.halfgallon.withcon.domain.auth.entity.AccessToken;
import com.halfgallon.withcon.domain.auth.entity.RefreshToken;
import com.halfgallon.withcon.domain.auth.repository.AccessTokenRepository;
import com.halfgallon.withcon.domain.auth.repository.RefreshTokenRepository;
import com.halfgallon.withcon.global.annotation.WithCustomMockUser;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Disabled
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LogoutTest {

  private final String LOGOUT_PATH = "/auth/logout";

  @Autowired
  private AccessTokenRepository accessTokenRepository;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithCustomMockUser(id = Long.MAX_VALUE)
  @DisplayName("로그아웃 시 액세스토큰과 리프래시 토큰이 Redis 에서 삭제된다.")
  void logout_Success() throws Exception {
    // given
    Long memberId = Long.MAX_VALUE;
    AccessToken accessToken = new AccessToken(memberId, "accessToken");
    RefreshToken refreshToken = new RefreshToken(memberId, "refreshToken");

    accessTokenRepository.save(accessToken);
    refreshTokenRepository.save(refreshToken);

    // when
    mockMvc.perform(post(LOGOUT_PATH))
        .andExpect(status().isOk());

    // then
    assertThat(accessTokenRepository.findById(memberId).isEmpty()).isTrue();
    assertThat(refreshTokenRepository.findById(memberId).isEmpty()).isTrue();
  }

  @Test
  @WithCustomMockUser(id = Long.MAX_VALUE)
  @DisplayName("로그아웃 시 리프래시 토큰 쿠키가 삭제된다.")
  void logout_Success_Will_Delete_RefreshTokenCookie() throws Exception {
    // given
    Cookie requestCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, "refreshToken");
    requestCookie.setMaxAge(500);

    // when
    // then
    mockMvc.perform(post(LOGOUT_PATH)
            .cookie(requestCookie))
        .andExpect(status().isOk())
        .andExpect(cookie().maxAge(REFRESH_TOKEN_COOKIE_NAME, 0));
  }
}