package com.halfgallon.withcon.domain.chat.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {
  ENTER("입장"),
  CHAT("채팅"),
  EXIT("퇴장"),
  KICK("강퇴");

  private final String description;
}
