package com.halfgallon.withcon.domain.auth.client;

public interface OAuth2Service {

  String generateAccessToken(String authorizationCode);

  OAuth2UserInfo getUserInfo(String accessToken);

  String supports();

}
