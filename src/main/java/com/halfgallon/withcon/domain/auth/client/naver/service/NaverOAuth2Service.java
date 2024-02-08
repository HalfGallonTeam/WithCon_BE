package com.halfgallon.withcon.domain.auth.client.naver.service;

import static com.nimbusds.oauth2.sdk.GrantType.AUTHORIZATION_CODE;

import com.halfgallon.withcon.domain.auth.client.OAuth2UserInfo;
import com.halfgallon.withcon.domain.auth.client.naver.NaverLoginClient;
import com.halfgallon.withcon.domain.auth.client.naver.NaverUserInfoClient;
import com.halfgallon.withcon.domain.auth.client.naver.dto.request.NaverAccessTokenRequest;
import com.halfgallon.withcon.domain.auth.client.naver.dto.response.NaverAccessTokenResponse;
import com.halfgallon.withcon.domain.auth.client.naver.dto.response.NaverUserInfoResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NaverOAuth2Service {

  @Value("${oauth2.client.registration.naver.client-id}")
  private String clientId;

  @Value("${oauth2.client.registration.naver.client-secret}")
  private String clientSecret;

  private final NaverLoginClient naverLoginClient;

  private final NaverUserInfoClient naverUserInfoClient;

  public String generateAccessToken(String authorizationCode) {
    String state = UUID.randomUUID().toString();

    NaverAccessTokenResponse naverAccessTokenResponse = naverLoginClient.generateAccessToken(
        new NaverAccessTokenRequest(
            AUTHORIZATION_CODE.getValue(),
            clientId,
            clientSecret,
            authorizationCode,
            state));

    return naverAccessTokenResponse.getAccess_token();
  }

  public OAuth2UserInfo getUserInfo(String accessToken) {
    NaverUserInfoResponse response = naverUserInfoClient.getUserInfo(accessToken);
    return OAuth2UserInfo.ofNaver(response);
  }
}
