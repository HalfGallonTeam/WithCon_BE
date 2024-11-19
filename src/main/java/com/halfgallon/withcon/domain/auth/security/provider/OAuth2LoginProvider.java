package com.halfgallon.withcon.domain.auth.security.provider;

import static com.halfgallon.withcon.domain.auth.constant.AuthConstant.ACCESS_TOKEN_PREFIX;

import com.halfgallon.withcon.domain.auth.client.OAuth2Client;
import com.halfgallon.withcon.domain.auth.client.OAuth2UserInfo;
import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.auth.security.token.CustomOAuth2Token;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.global.exception.CustomException;
import com.halfgallon.withcon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class OAuth2LoginProvider implements AuthenticationProvider {

  private final OAuth2Client oAuth2Client;
  private final MemberRepository memberRepository;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    CustomOAuth2Token unauthenticatedToken = (CustomOAuth2Token) authentication;
    String registrationId = unauthenticatedToken.getRegistrationId();
    String authorizationCode = unauthenticatedToken.getAuthorizationCode();

    String accessToken = ACCESS_TOKEN_PREFIX +
        oAuth2Client.generateAccessToken(registrationId, authorizationCode);

    if (!StringUtils.hasText(accessToken)) {
      throw new CustomException(ErrorCode.INTERVAL_SERVER_ERROR);
    }

    OAuth2UserInfo userInfo = oAuth2Client.getUserInfo(registrationId, accessToken);

    Member member = saveOrFindMember(userInfo);

    CustomUserDetails principal = CustomUserDetails.fromEntity(member);

    return new CustomOAuth2Token(principal, accessToken);
  }

  /**
   * 신규 회원이면, 회원을 DB에 저장하고 조회 기존 회원이면, 회원을 조회
   */
  private Member saveOrFindMember(OAuth2UserInfo userInfo) {
    return memberRepository.findByUsername(userInfo.snsId())
        .orElseGet(() -> memberRepository.save(userInfo.toEntity()));
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (CustomOAuth2Token.class.isAssignableFrom(authentication));
  }
}
