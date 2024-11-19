package com.halfgallon.withcon.domain.auth.client.kakao.dto.response;

import lombok.Getter;

@Getter
public class KakaoUserInfoResponse {

  private Long id;

  private KakaoAccount kakao_account;

  @Getter
  public static class KakaoAccount {

    private Profile profile;

    @Getter
    public static class Profile {
      private String nickname;
    }
  }
}
