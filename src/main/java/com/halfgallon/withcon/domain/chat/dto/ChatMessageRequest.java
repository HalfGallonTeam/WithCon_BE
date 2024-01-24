package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.constant.MessageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageRequest {
  private Long memberId;
  private String message;
  private MessageType messageType;
}
