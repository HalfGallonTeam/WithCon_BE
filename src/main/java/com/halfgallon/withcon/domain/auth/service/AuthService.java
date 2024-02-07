package com.halfgallon.withcon.domain.auth.service;

import com.halfgallon.withcon.domain.auth.dto.request.AuthJoinRequest;

public interface AuthService {

  /**
   * 회원가입
   */
  void join(AuthJoinRequest authJoinRequest);

  /**
   * 액세스 토큰 재발급
   */
  String reissueAccessToken(String refreshToken);
}
