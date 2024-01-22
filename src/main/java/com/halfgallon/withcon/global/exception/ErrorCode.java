package com.halfgallon.withcon.global.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  INTERVAL_SERVER_ERROR(INTERNAL_SERVER_ERROR.value(), "내부 서버 오류가 발생하였습니다."),
  ;

  private final int status;

  private final String description;
}
