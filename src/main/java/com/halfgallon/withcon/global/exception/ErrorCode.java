package com.halfgallon.withcon.global.exception;

import static org.springframework.http.HttpStatus.CONTINUE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  /**
   * 400 Bad Request
   */
  USER_NOT_PARTICIPANT_CHATTING(BAD_REQUEST.value(), "참여하고 있는 채팅이 없습니다."),
  ALREADY_PARTICIPANT_CHATTING(BAD_REQUEST.value(), "이미 참여하고 있는 채팅입니다."),
  USER_JUST_ONE_CREATE_CHATROOM(BAD_REQUEST.value(), "채팅방은 1인당 1개만 생성 가능합니다."),
  METHOD_NOT_SUPPORTED(BAD_REQUEST.value(), "잘못된 Method 요청입니다."),
  CONTENT_TYPE_NOT_SUPPORTED(BAD_REQUEST.value(), "잘못된 Content-type 요청입니다."),
  /**
   * 401 Unauthorized
   */


  /**
   * 404 Not Found
   */
  USER_NOT_FOUND(NOT_FOUND.value(), "유저가 아닙니다"),
  CHATROOM_NOT_FOUND(NOT_FOUND.value(), "채팅방이 생성되지 않았습니다."),
  PARTICIPANT_NOT_FOUND(NOT_FOUND.value(), "해당 채팅방 참여자가 아닙니다."),

  /**
   * 409 conflict
   */
  DUPLICATE_CHATROOM(CONFLICT.value(), "이미 존재하는 채팅방입니다."),
  DUPLICATE_EMAIL(CONFLICT.value(), "이미 사용하고 있는 이메일입니다."),
  DUPLICATE_USERNAME(CONTINUE.value(), "이미 사용하고 있는 ID 입니다."),
  DUPLICATE_NICKNAME(CONTINUE.value(), "이미 사용하고 있는 닉네임입니다."),
  DUPLICATE_PHONE_NUMBER(CONTINUE.value(), "이미 사용하고 있는 핸드폰 번호입니다."),

  /**
   * 500 Internal Server Error
   */
  INTERVAL_SERVER_ERROR(INTERNAL_SERVER_ERROR.value(), "내부 서버 오류가 발생하였습니다."),
  ;

  private final int status;

  private final String description;
}
