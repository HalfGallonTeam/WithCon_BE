package com.halfgallon.withcon.domain.auth.security.token;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import java.util.Collections;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class CustomOAuth2Token extends AbstractAuthenticationToken {

  private CustomUserDetails principal;

  private String registrationId;

  private String authorizationCode;

  private String accessToken;


  /**
   * 인증되지 않은 토큰 생성
   *
   * @param registrationId    : OAuth2 클라이언트 (e.g "kakao")
   * @param authorizationCode : 인증 코드
   */
  public CustomOAuth2Token(String registrationId, String authorizationCode) {
    super(Collections.emptyList());
    this.registrationId = registrationId;
    this.authorizationCode = authorizationCode;
    this.setAuthenticated(false);
  }

  /**
   * 인증된 토큰 생성
   *
   * @param principal   : 액세스토큰으로 kakao 에서 가져온 userInfo
   * @param accessToken : 인증 코드로 OAuth2 클라이언트에 인증되면 발급해주는 액세스토큰.
   */
  public CustomOAuth2Token(CustomUserDetails principal, String accessToken) {
    super(Collections.emptyList());
    this.principal = principal;
    this.accessToken = accessToken;
    this.setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return "";
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }

  public String getRegistrationId() {
    return this.registrationId;
  }

  public String getAuthorizationCode() {
    return this.authorizationCode;
  }

  public String getAccessToken() {
    return this.accessToken;
  }
}
