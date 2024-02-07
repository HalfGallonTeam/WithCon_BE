package com.halfgallon.withcon.domain.auth.client.naver.dto.response;

import lombok.Getter;

@Getter
public class NaverUserInfoResponse {

  private String resultcode;
  private String message;
  private Response response;

  @Getter
  public static class Response {
    private String id;
    private String nickname;
    private String name;
    private String email;
    private String gender;
    private String age;
    private String birthday;
    private String profile_image;
    private String birthyear;
    private String mobile;
  }
}
