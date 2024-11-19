package com.halfgallon.withcon.domain.auth.controller;

import static com.halfgallon.withcon.domain.auth.constant.AuthConstant.ACCESS_TOKEN_HEADER_NAME;
import static com.halfgallon.withcon.domain.auth.constant.AuthConstant.REFRESH_TOKEN_COOKIE_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfgallon.withcon.domain.auth.dto.request.AuthJoinRequest;
import com.halfgallon.withcon.domain.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = AuthController.class,
    excludeAutoConfiguration = {
        UserDetailsServiceAutoConfiguration.class,
        SecurityAutoConfiguration.class
    },
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = {
                SecurityConfig.class, AuthenticationFilter.class
            })
    }
)
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private AuthService authService;

  @Test
  @DisplayName("회원가입 요청")
  void join_Success() throws Exception {
    AuthJoinRequest request = AuthJoinRequest.builder()
        .username("username")
        .password("1q2w3e4r!")
        .nickname("위드콘")
        .phoneNumber("01012345678")
        .build();

    mockMvc.perform(post("/auth/join")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    verify(authService, times(1)).join(any(AuthJoinRequest.class));
  }

  @Test
  @DisplayName("username 중복 체크 요청")
  void usernameDuplicationCheck_Success() throws Exception {
    String username = "username";

    mockMvc.perform(post("/auth/username-duplication-check")
            .contentType(MediaType.APPLICATION_JSON)
            .content(username))
        .andExpect(status().isOk());

    verify(authService, times(1)).usernameDuplicationCheck(username);
  }

  @Test
  @DisplayName("phoneNumber 중복 체크 요청")
  void phoneNumberDuplicationCheck_Success() throws Exception {
    String phoneNumber = "010-1234-5678";

    mockMvc.perform(post("/auth/phone-number-duplication-check")
            .contentType(MediaType.APPLICATION_JSON)
            .content(phoneNumber))
        .andExpect(status().isOk());

    verify(authService, times(1)).phoneNumberDuplicationCheck(phoneNumber);
  }

  @Test
  @DisplayName("액세스토큰 재발급 요청")
  void reissueAccessToken_Success() throws Exception {
    String refreshToken = "RefreshToken";

    mockMvc.perform(post("/auth/reissue")
            .contentType(MediaType.APPLICATION_JSON)
            .cookie(new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken)))
        .andExpect(header().exists(ACCESS_TOKEN_HEADER_NAME))
        .andExpect(status().isOk());

    verify(authService, times(1)).reissueAccessToken(refreshToken);
  }
}