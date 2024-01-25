package com.halfgallon.withcon.global.exception;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CONTINUE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // 500
  INTERVAL_SERVER_ERROR(INTERNAL_SERVER_ERROR.value(), "내부 서버 오류가 발생하였습니다."),

  // 409
  DUPLICATE_EMAIL(CONFLICT.value(), "이미 사용하고 있는 이메일입니다."),
  DUPLICATE_USERNAME(CONTINUE.value(), "이미 사용하고 있는 ID 입니다."),
  DUPLICATE_NICKNAME(CONTINUE.value(), "이미 사용하고 있는 닉네임입니다."),
  DUPLICATE_PHONE_NUMBER(CONTINUE.value(), "이미 사용하고 있는 핸드폰 번호입니다."),
  ;

  private final int status;

  private final String description;
}
