package com.halfgallon.withcon.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

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
  INVALID_PARAMETER(BAD_REQUEST.value(), "유효하지 않은 파라미터 값입니다."),
  CONTENT_TYPE_NOT_SUPPORTED(BAD_REQUEST.value(), "잘못된 Content-type 요청입니다."),
  LOGIN_FAILURE_MESSAGE(BAD_REQUEST.value(), "아이디 혹은 비밀번호가 올바르지 않습니다."),
  OAUTH2_LOGIN_FAILURE_MESSAGE(BAD_REQUEST.value(), "소셜 로그인에 실패하셨습니다."),
  CURRENT_PASSWORD_MISMATCH(BAD_REQUEST.value(), "현재 비밀번호와 일치하지 않습니다."),

  /**
   * 401 Unauthorized
   */
  ACCESS_TOKEN_EXPIRED(UNAUTHORIZED.value(), "액세스 토큰이 만료되었습니다."),
  REFRESH_TOKEN_EXPIRED(UNAUTHORIZED.value(), "리프래시 토큰이 만료되었습니다."),
  ACCESS_TOKEN_NOT_SUPPORTED(UNAUTHORIZED.value(), "잘못된 액세스 토큰 요청입니다."),
  REFRESH_TOKEN_COOKIE_IS_EMPTY(UNAUTHORIZED.value(), "리프래시 토큰 쿠키가 없는 요청입니다."),
  JWT_PARSE_ERROR(UNAUTHORIZED.value(), "만료되었거나 유효하지 않은 JWT 토큰 입니다."),

  /**
   * 404 Not Found
   */
  MEMBER_NOT_FOUND(NOT_FOUND.value(), "존재하지 않는 회원입니다."),
  CHATROOM_NOT_FOUND(NOT_FOUND.value(), "채팅방이 생성되지 않았습니다."),
  PARTICIPANT_NOT_FOUND(NOT_FOUND.value(), "해당 채팅방 참여자가 아닙니다."),
  PERFORMANCE_NOT_FOUND(NOT_FOUND.value(), "존재하지 않는 공연입니다."),
  NOT_EXIST_LIKE(NOT_FOUND.value(), "찜이 존재하지 않습니다."),
  NOT_EXIST_NOTIFICATION(NOT_FOUND.value(), "알림이 존재하지 않습니다."),
  GENRE_NOT_FOUND(NOT_FOUND.value(), "존재하지 않는 장르입니다."),
  PERFORMANCE_DETAIL_NOT_FOUND(NOT_FOUND.value(), "존재하지 않는 공연 세부 정보입니다."),

  /**
   * 409 conflict
   */
  DUPLICATE_CHATROOM_NAME(CONFLICT.value(), "이미 사용하는 채팅방 이름입니다."),
  DUPLICATE_USERNAME(CONFLICT.value(), "이미 사용하고 있는 ID 입니다."),
  DUPLICATE_PHONE_NUMBER(CONFLICT.value(), "이미 사용하고 있는 핸드폰 번호입니다."),
  ALREADY_LIKE_EXIST(CONFLICT.value(), "이미 찜한 공연입니다."),

  /**
   * 500 Internal Server Error
   */
  INTERVAL_SERVER_ERROR(INTERNAL_SERVER_ERROR.value(), "내부 서버 오류가 발생하였습니다."),
  ;

  private final int status;

  private final String description;
}
