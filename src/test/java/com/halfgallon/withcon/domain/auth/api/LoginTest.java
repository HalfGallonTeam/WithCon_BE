package com.halfgallon.withcon.domain.auth.api;

import static com.halfgallon.withcon.domain.auth.constant.AuthConstant.ACCESS_TOKEN_HEADER_NAME;
import static com.halfgallon.withcon.domain.auth.constant.AuthConstant.REFRESH_TOKEN_COOKIE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfgallon.withcon.domain.auth.repository.AccessTokenRepository;
import com.halfgallon.withcon.domain.auth.repository.RefreshTokenRepository;
import com.halfgallon.withcon.domain.auth.security.filter.LoginFilter.LoginRequest;
import com.halfgallon.withcon.domain.member.constant.LoginType;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.global.exception.ErrorCode;
import com.halfgallon.withcon.global.exception.ErrorResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Disabled
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginTest {

  private final String LOGIN_PATH = "/auth/login";

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AccessTokenRepository accessTokenRepository;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("❗로그인 시 아이디가 틀리면 에러를 응답한다.")
  void login_Fails_When_Username_Not_Match() throws Exception {
    // given
    String encodedPassword = passwordEncoder.encode("password");

    Member member = createDefaultMember(encodedPassword);

    memberRepository.save(member);

    LoginRequest loginRequest = new LoginRequest("error-username", "password");

    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
        ErrorCode.LOGIN_FAILURE_MESSAGE, ErrorCode.LOGIN_FAILURE_MESSAGE.getDescription());

    // when
    // then
    mockMvc.perform(post(LOGIN_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().json(objectMapper.writeValueAsString(errorResponse)));
  }

  @Test
  @DisplayName("❗로그인 시 비밀번호가 틀리면 에러를 응답한다.")
  void login_Fails_When_Password_Not_Match() throws Exception {
    // given
    String encodedPassword = passwordEncoder.encode("password");

    Member member = createDefaultMember(encodedPassword);

    memberRepository.save(member);

    LoginRequest loginRequest = new LoginRequest("username", "error-password");

    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
        ErrorCode.LOGIN_FAILURE_MESSAGE, ErrorCode.LOGIN_FAILURE_MESSAGE.getDescription());
    // when
    // then
    mockMvc.perform(post(LOGIN_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().json(objectMapper.writeValueAsString(errorResponse)));
  }

  @Test
  @DisplayName("로그인 성공 시 액세스토큰과 리프래시 토큰이 발급된다.")
  void login_Success() throws Exception {
    // given
    String encodedPassword = passwordEncoder.encode("password");

    Member member = createDefaultMember(encodedPassword);

    memberRepository.save(member);

    LoginRequest loginRequest = new LoginRequest("username", "password");

    // when
    // then
    mockMvc.perform(post(LOGIN_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(header().exists(ACCESS_TOKEN_HEADER_NAME))
        .andExpect(cookie().exists(REFRESH_TOKEN_COOKIE_NAME))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("로그인 성공 시 액세스토큰과 리프래시 토큰이 회원 id를 키 값으로 Redis에 저장된다.")
  void login_Success_Will_Save_Token_In_Redis() throws Exception {
    // given
    String encodedPassword = passwordEncoder.encode("password");

    Member member = createDefaultMember(encodedPassword);

    Member savedMember = memberRepository.save(member);

    LoginRequest loginRequest = new LoginRequest("username", "password");

    // when
    // then
    mockMvc.perform(post(LOGIN_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(header().exists(ACCESS_TOKEN_HEADER_NAME))
        .andExpect(cookie().exists(REFRESH_TOKEN_COOKIE_NAME))
        .andExpect(status().isOk());

    boolean existAccessToken = accessTokenRepository.findById(savedMember.getId())
        .isPresent();
    boolean existRefreshToken = refreshTokenRepository.findById(savedMember.getId())
        .isPresent();

    assertThat(existAccessToken).isTrue();
    assertThat(existRefreshToken).isTrue();
  }

  private static Member createDefaultMember(String encodedPassword) {
    return Member.builder()
        .username("username")
        .email("test@example.com")
        .nickname("위드콘")
        .loginType(LoginType.HOME)
        .password(encodedPassword)
        .phoneNumber("01012345678")
        .build();
  }
}