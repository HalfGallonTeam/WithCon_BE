package com.halfgallon.withcon.domain.auth.security.filter;

import static com.halfgallon.withcon.domain.auth.constant.AuthConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.halfgallon.withcon.domain.auth.entity.AccessToken;
import com.halfgallon.withcon.domain.auth.repository.AccessTokenRepository;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.global.exception.CustomException;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class JwtAuthenticationFilterTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private AccessTokenRepository accessTokenRepository;

  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @BeforeEach
  public void setUp() {
    jwtAuthenticationFilter = new JwtAuthenticationFilter(memberRepository, accessTokenRepository);
  }

  @AfterEach
  public void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("헤더에 액세스토큰이 없는 요청이면 다음 필터를 호출한다.")
  public void doFilterInternal_Access_Token_empty()
      throws ServletException, IOException {

    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain chain = new MockFilterChain();

    // when
    jwtAuthenticationFilter.doFilterInternal(request, response, chain);

    // then
    assertThat(chain.getRequest()).isEqualTo(request);
    assertThat(chain.getResponse()).isEqualTo(response);
  }

  @Test
  @DisplayName("❗헤더에 액세스토큰이 있지만 'Bearer' 로 시작하지 않으면 실패한다.")
  public void doFilterInternal_Fail_Access_Token_Prefix_Is_Not_Bearer()
      throws ServletException, IOException {

    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ACCESS_TOKEN_HEADER_NAME, "No Bearer AccessToken");

    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain chain = new MockFilterChain();

    // when
    // then
    assertThatThrownBy(() -> jwtAuthenticationFilter.doFilterInternal(request, response, chain))
        .isInstanceOf(CustomException.class)
        .hasMessageContaining("잘못된 액세스 토큰 요청입니다.");
  }

  @Test
  @DisplayName("❗헤더의 액세스 토큰이 저장된 액세스 토큰이 아니면 액세스 토큰이 만료되었다고 응답한다.")
  public void doFilterInternal_Fail_Access_Token_Expired()
      throws ServletException, IOException {

    String accessTokenValue = "AccessToken";

    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ACCESS_TOKEN_HEADER_NAME, ACCESS_TOKEN_PREFIX + accessTokenValue);

    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain chain = new MockFilterChain();

    // when
    // then
    assertThatThrownBy(() -> jwtAuthenticationFilter.doFilterInternal(request, response, chain))
        .isInstanceOf(CustomException.class)
        .hasMessageContaining("액세스 토큰이 만료되었습니다.");
  }

  @Test
  @DisplayName("❗헤더의 액세스 토큰의 주인이 존재하지 않는 회원이면 예외를 던진다.")
  public void doFilterInternal_Fail_Member_Not_Found()
      throws ServletException, IOException {

    // given
    String accessTokenValue = "AccessToken";
    AccessToken accessToken = new AccessToken(1L, accessTokenValue);

    given(accessTokenRepository.findByAccessToken(accessTokenValue)).willReturn(
        Optional.of(accessToken));
    given(memberRepository.findById(1L)).willReturn(Optional.empty());

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ACCESS_TOKEN_HEADER_NAME, ACCESS_TOKEN_PREFIX + accessTokenValue);

    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain chain = new MockFilterChain();

    // when
    // then
    assertThatThrownBy(() -> jwtAuthenticationFilter.doFilterInternal(request, response, chain))
        .isInstanceOf(CustomException.class)
        .hasMessageContaining("존재하지 않는 회원입니다.");
  }

  @Test
  @DisplayName("액세스 토큰 검증에 성공하면 인증 객체가 시큐리티 컨텍스트에 저장된다.")
  public void doFilterInternal_Success()
      throws ServletException, IOException {

    // given
    String accessTokenValue = "AccessToken";
    AccessToken accessToken = new AccessToken(1L, accessTokenValue);

    Member member = Member.builder().id(1L).build();

    given(accessTokenRepository.findByAccessToken(accessTokenValue)).willReturn(
        Optional.of(accessToken));
    given(memberRepository.findById(1L)).willReturn(Optional.of(member));

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(ACCESS_TOKEN_HEADER_NAME, ACCESS_TOKEN_PREFIX + accessTokenValue);

    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain chain = new MockFilterChain();

    // when
    jwtAuthenticationFilter.doFilterInternal(request, response, chain);

    // then
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    assertThat(authentication).isNotNull();
    assertThat(chain.getRequest()).isEqualTo(request);
    assertThat(chain.getResponse()).isEqualTo(response);
  }
}
