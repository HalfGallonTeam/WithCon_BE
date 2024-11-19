package com.halfgallon.withcon.domain.auth.client.naver.dto.response;

import lombok.Getter;

@Getter
public class NaverAccessTokenResponse {

  private String access_token;
  private String refresh_token;
  private String token_type;
  private Integer expires_in;
  private String error;
  private String error_description;
}