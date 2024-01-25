package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.constant.MessageType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageResponse {

  private Long memberId;
  private Long chatRoomId;

  private String message;
  private MessageType messageType;

  private LocalDateTime time;
}
