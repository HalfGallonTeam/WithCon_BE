package com.halfgallon.withcon.domain.auth.client;

import static com.halfgallon.withcon.domain.member.constant.LoginType.*;

import com.halfgallon.withcon.domain.auth.client.kakao.service.KakaoOAuth2Service;
import com.halfgallon.withcon.domain.auth.client.naver.service.NaverOAuth2Service;
import com.halfgallon.withcon.domain.member.constant.LoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2Client {

  private final NaverOAuth2Service naverOAuth2Service;
  private final KakaoOAuth2Service kakaoOAuth2Service;

  public String generateAccessToken(String registrationId, String authorizationCode) {
    String accessToken = null;

    if (NAVER.name().equalsIgnoreCase(registrationId)) {
      accessToken = naverOAuth2Service.generateAccessToken(authorizationCode);
    } else if (KAKAO.name().equalsIgnoreCase(registrationId)) {
      accessToken = kakaoOAuth2Service.generateAccessToken(authorizationCode);
    }
    return accessToken;
  }

  public OAuth2UserInfo getUserInfo(String registrationId, String accessToken) {
    OAuth2UserInfo userInfo = null;

    if (NAVER.name().equalsIgnoreCase(registrationId)) {
      userInfo = naverOAuth2Service.getUserInfo(accessToken);
    } else if (KAKAO.name().equalsIgnoreCase(registrationId)) {
      userInfo = kakaoOAuth2Service.getUserInfo(accessToken);
    }
    return userInfo;
  }
}
