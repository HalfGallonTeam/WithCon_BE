package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatLastReadMessage {

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class Request {
    private Long memberId;
    private Long chatRoomId;
  }

  @Getter
  @Builder
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class Response {
    private Long memberId;
    private Long chatRoomId;
    private Long lastMsgId;

    public static ChatLastReadMessage.Response fromEntity(ChatParticipant chatParticipant) {
      return ChatLastReadMessage.Response.builder()
          .memberId(chatParticipant.getMember().getId())
          .chatRoomId(chatParticipant.getChatRoom().getId())
          .lastMsgId(chatParticipant.getLastReadId())
          .build();
    }
  }

}
