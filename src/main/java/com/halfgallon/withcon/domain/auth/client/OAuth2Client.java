package com.halfgallon.withcon.domain.auth.client;

import static com.halfgallon.withcon.domain.member.constant.LoginType.*;

import com.halfgallon.withcon.domain.auth.client.kakao.service.KakaoOAuth2Service;
import com.halfgallon.withcon.domain.auth.client.naver.service.NaverOAuth2Service;
import com.halfgallon.withcon.domain.member.constant.LoginType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2Client {

  private final List<OAuth2Service> oAuth2Services;

  public String generateAccessToken(String registrationId, String authorizationCode) {
    String accessToken = null;

    for (OAuth2Service service : oAuth2Services) {
      if (registrationId.equals(service.supports())) {
        accessToken = service.generateAccessToken(authorizationCode);
      }
    }
    return accessToken;
  }

  public OAuth2UserInfo getUserInfo(String registrationId, String accessToken) {
    OAuth2UserInfo userInfo = null;

    for (OAuth2Service service : oAuth2Services) {
      if (registrationId.equals(service.supports())) {
        userInfo = service.getUserInfo(accessToken);
      }
    }
    return userInfo;
  }
}
