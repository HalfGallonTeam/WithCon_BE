package com.halfgallon.withcon.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.halfgallon.withcon.domain.auth.dto.request.AuthJoinRequest;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.global.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
class AuthServiceImplTest {

  @InjectMocks
  private AuthServiceImpl authService;

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Captor
  private ArgumentCaptor<Member> memberCaptor;

  @Test
  @DisplayName("❗️회원가입 시 중복된 username 이면 실패한다.")
  void join_FailByDuplicateUsername() {
    // given
    AuthJoinRequest request = AuthJoinRequest.builder()
        .build();

    // when
    given(memberRepository.existsByUsername(any())).willReturn(true);

    // then
    assertThatThrownBy(() -> authService.join(request))
        .isInstanceOf(CustomException.class)
        .hasMessageContaining("이미 사용하고 있는 ID 입니다.");
  }

  @Test
  @DisplayName("❗️회원가입 시 중복된 email 이면 실패한다.")
  void join_FailByDuplicateEmail() {
    // given
    AuthJoinRequest request = AuthJoinRequest.builder()
        .build();

    // when
    given(memberRepository.existsByEmail(any())).willReturn(true);

    // then
    assertThatThrownBy(() -> authService.join(request))
        .isInstanceOf(CustomException.class)
        .hasMessageContaining("이미 사용하고 있는 이메일입니다.");
  }

  @Test
  @DisplayName("❗️회원가입 시 중복된 nickname 이면 실패한다.")
  void join_FailByDuplicateNickname() {
    // given
    AuthJoinRequest request = AuthJoinRequest.builder()
        .build();

    // when
    given(memberRepository.existsByNickname(any())).willReturn(true);

    // then
    assertThatThrownBy(() -> authService.join(request))
        .isInstanceOf(CustomException.class)
        .hasMessageContaining("이미 사용하고 있는 닉네임입니다.");
  }

  @Test
  @DisplayName("❗️회원가입 시 중복된 phoneNumber 이면 실패한다.")
  void join_FailByDuplicatePhoneNumber() {
    // given
    AuthJoinRequest request = AuthJoinRequest.builder()
        .build();

    // when
    given(memberRepository.existsByPhoneNumber(any())).willReturn(true);

    // then
    assertThatThrownBy(() -> authService.join(request))
        .isInstanceOf(CustomException.class)
        .hasMessageContaining("이미 사용하고 있는 핸드폰 번호입니다.");
  }

  @Test
  @DisplayName("회원가입에 성공하면 회원의 패스워드는 인코딩 되어 저장된다.")
  void join_Success() {
    // given
    AuthJoinRequest request = AuthJoinRequest.builder()
        .username("username")
        .password("1q2w3e4r")
        .nickname("nickname")
        .email("test@example.com")
        .phoneNumber("01012345678")
        .build();

    when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

    // when
    authService.join(request);

    // then
    verify(memberRepository).save(memberCaptor.capture());

    Member savedMember = memberCaptor.getValue();
    Assertions.assertThat(savedMember.getPassword()).isEqualTo("encodedPassword");
  }
}