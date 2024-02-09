package com.halfgallon.withcon.domain.notification.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationMessage {

  SUBSCRIBE("SSE 구독 완료"),
  ENTER_CHATROOM("님이 입장하였습니다."),
  EXIT_CHATROOM("님이 퇴장하였습니다"),
  DROP_CHATROOM("님이 강퇴당했습니다."),
  NEW_MESSAGE_FROM_CHATROOM("새로운 메세지가 있습니다.");

  private final String description;

}