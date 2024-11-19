package com.halfgallon.withcon.domain.auth.client.naver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NaverAccessTokenRequest {

  private String grant_type;
  private String client_id;
  private String client_secret;
  private String code;
  private String state;
}