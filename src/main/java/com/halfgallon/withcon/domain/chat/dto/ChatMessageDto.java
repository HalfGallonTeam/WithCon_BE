package com.halfgallon.withcon.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.halfgallon.withcon.domain.chat.constant.MessageType;
import com.halfgallon.withcon.domain.chat.entity.ChatMessage;
import java.time.LocalDateTime;
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

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime sendAt;

  public ChatMessage toEntity() {
    return ChatMessage.builder()
        .message(this.message)
        .messageType(this.messageType)
        .sendAt(this.sendAt)
        .build();
  }

  public static ChatMessageDto fromEntity(ChatMessage chatMessage) {
    return ChatMessageDto.builder()
        .roomId(chatMessage.getRoom().getId())
        .memberId(chatMessage.getChatParticipant().getId())
        .message(chatMessage.getMessage())
        .messageType(chatMessage.getMessageType())
        .sendAt(chatMessage.getSendAt())
        .build();
  }
}
