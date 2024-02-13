package com.halfgallon.withcon.domain.auth.service;

import com.halfgallon.withcon.domain.auth.dto.request.AuthJoinRequest;

public interface AuthService {

  /**
   * 회원가입
   */
  void join(AuthJoinRequest authJoinRequest);

  /**
   * username 중복 체크
   */
  void usernameDuplicationCheck(String username);

  /**
   * phoneNumber 중복 체크
   */
  void phoneNumberDuplicationCheck(String phoneNumber);

  /**
   * 액세스 토큰 재발급
   */
  String reissueAccessToken(String refreshToken);


}
