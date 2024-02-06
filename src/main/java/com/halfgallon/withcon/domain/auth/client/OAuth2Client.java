package com.halfgallon.withcon.domain.auth.client;

import com.halfgallon.withcon.domain.auth.client.kakao.service.KakaoOAuth2Service;
import com.halfgallon.withcon.domain.auth.client.naver.service.NaverOAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2Client {

  private final NaverOAuth2Service naverOAuth2Service;
  private final KakaoOAuth2Service kakaoOAuth2Service;

  public String generateAccessToken(String registrationId, String authorizationCode) {
    String accessToken = null;

    if ("naver".equalsIgnoreCase(registrationId)) {
      accessToken = naverOAuth2Service.generateAccessToken(authorizationCode);
    } else if ("kakao".equalsIgnoreCase(registrationId)) {
      accessToken = kakaoOAuth2Service.generateAccessToken(authorizationCode);
    }
    return accessToken;
  }

  public OAuth2UserInfo getUserInfo(String registrationId, String accessToken) {
    OAuth2UserInfo userInfo = null;

    if ("naver".equalsIgnoreCase(registrationId)) {
      userInfo = naverOAuth2Service.getUserInfo(accessToken);
    } else if ("kakao".equalsIgnoreCase(registrationId)) {
      userInfo = kakaoOAuth2Service.getUserInfo(accessToken);
    }
    return userInfo;
  }
}
