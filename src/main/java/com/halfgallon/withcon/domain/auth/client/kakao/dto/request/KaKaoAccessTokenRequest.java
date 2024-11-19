package com.halfgallon.withcon.domain.auth.client.kakao.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KaKaoAccessTokenRequest {

  private String grant_type;
  private String client_id;
  private String redirect_uri;
  private String code;
  private String client_secret;

}
