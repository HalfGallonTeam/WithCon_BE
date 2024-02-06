package com.halfgallon.withcon.domain.auth.client.kakao.service;

import static com.nimbusds.oauth2.sdk.GrantType.AUTHORIZATION_CODE;

import com.halfgallon.withcon.domain.auth.client.OAuth2UserInfo;
import com.halfgallon.withcon.domain.auth.client.kakao.KakaoLoginClient;
import com.halfgallon.withcon.domain.auth.client.kakao.KakaoUserInfoClient;
import com.halfgallon.withcon.domain.auth.client.kakao.dto.request.KaKaoAccessTokenRequest;
import com.halfgallon.withcon.domain.auth.client.kakao.dto.response.KakaoAccessTokenResponse;
import com.halfgallon.withcon.domain.auth.client.kakao.dto.response.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoOAuth2Service {

  @Value("${oauth2.client.registration.kakao.client-id}")
  private String clientId;

  @Value("${oauth2.client.registration.kakao.client-secret}")
  private String clientSecret;

  @Value("${oauth2.client.registration.kakao.redirect-uri}")
  private String redirectUri;

  private static final String URLENCODED_UTF_8 = "application/x-www-form-urlencoded;charset=utf-8";

  private final KakaoLoginClient kakaoLoginClient;
  private final KakaoUserInfoClient kakaoUserInfoClient;

  public String generateAccessToken(String authorizationCode) {
    KakaoAccessTokenResponse kakaoAccessTokenResponse = kakaoLoginClient.generateAccessToken(
        URLENCODED_UTF_8,
        new KaKaoAccessTokenRequest(AUTHORIZATION_CODE.getValue(), clientId, redirectUri,
            authorizationCode, clientSecret));

    return kakaoAccessTokenResponse.getAccess_token();
  }

  public OAuth2UserInfo getUserInfo(String accessToken) {
    KakaoUserInfoResponse response = kakaoUserInfoClient.getUserInfo(URLENCODED_UTF_8, accessToken);
    return OAuth2UserInfo.ofKakao(response);
  }
}
