package com.halfgallon.withcon.domain.auth.client.kakao.dto.response;

import lombok.Getter;

@Getter
public class KakaoAccessTokenResponse {
  private String token_type;
  private String access_token;
  private Integer expires_in;
  private String refresh_token;
  private String refresh_token_expires_in;
}