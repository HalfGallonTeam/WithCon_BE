package com.halfgallon.withcon.domain.auth.service;

import com.halfgallon.withcon.domain.auth.dto.request.AuthJoinRequest;

public interface AuthService {

  void join(AuthJoinRequest authJoinRequest);
}
