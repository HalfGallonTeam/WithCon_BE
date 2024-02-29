package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.constant.MessageType;
import com.halfgallon.withcon.domain.chat.entity.ChatMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageDto {
  private Long memberId;
  private Long roomId;

  private String message;
  private MessageType messageType;

  private String nickName;
  private String userProfile;

  private Long sendAt;

  public ChatMessage toEntity() {
    return ChatMessage.builder()
        .message(this.message)
        .messageType(this.messageType)
        .sendAt(this.sendAt)
        .build();
  }

}
