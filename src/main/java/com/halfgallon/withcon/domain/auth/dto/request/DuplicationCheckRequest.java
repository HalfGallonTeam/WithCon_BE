package com.halfgallon.withcon.domain.auth.dto.request;

import lombok.Getter;

@Getter
public class DuplicationCheckRequest {
  private String username;

  private String phoneNumber;

}
