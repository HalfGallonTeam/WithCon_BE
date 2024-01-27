package com.halfgallon.withcon.domain.auth.security.filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfgallon.withcon.domain.auth.security.filter.LoginFilter.LoginRequest;
import com.halfgallon.withcon.domain.member.constant.LoginType;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
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
class LoginRequestTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("❗로그인 시 아이디가 틀리면 에러를 응답한다.")
  void login_Fails_When_Username_Not_Match() throws Exception {
    // given
    String encodedPassword = passwordEncoder.encode("password");

    Member member = Member.builder()
        .username("username")
        .email("test@example.com")
        .nickname("위드콘")
        .loginType(LoginType.HOME)
        .password(encodedPassword)
        .phoneNumber("01012345678")
        .build();

    memberRepository.save(member);

    LoginRequest loginRequest = new LoginRequest("error-username", "password");

    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
        "아이디 혹은 비밀번호가 올바르지 않습니다.");
    // when
    // then
    mockMvc.perform(post("/auth/login")
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

    Member member = Member.builder()
        .username("username")
        .email("test@example.com")
        .nickname("위드콘")
        .loginType(LoginType.HOME)
        .password(encodedPassword)
        .phoneNumber("01012345678")
        .build();

    memberRepository.save(member);

    LoginRequest loginRequest = new LoginRequest("username", "error-password");

    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
        "아이디 혹은 비밀번호가 올바르지 않습니다.");
    // when
    // then
    mockMvc.perform(post("/auth/login")
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

    Member member = Member.builder()
        .username("username")
        .email("test@example.com")
        .nickname("위드콘")
        .loginType(LoginType.HOME)
        .password(encodedPassword)
        .phoneNumber("01012345678")
        .build();

    memberRepository.save(member);

    LoginRequest loginRequest = new LoginRequest("username", "password");

    // when
    // then
    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(header().exists("Authorization"))
        .andExpect(cookie().exists("refresh_token"))
        .andExpect(status().isOk());
  }
}