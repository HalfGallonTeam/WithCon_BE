package com.halfgallon.withcon.domain.notification.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationMessage {

  SUBSCRIBE("{\"message\":\"SSE 구독완료 memberId : "),
  ENTER_CHATROOM("님이 입장하였습니다."),
  EXIT_CHATROOM("님이 퇴장하였습니다"),
  DROP_CHATROOM("님이 강퇴당했습니다."),
  NEW_MESSAGE_FROM_CHATROOM("새로운 메세지가 있습니다."),
  OPEN_PERFORMANCE(" 공연 당일 입니다.");

  private final String description;

}
