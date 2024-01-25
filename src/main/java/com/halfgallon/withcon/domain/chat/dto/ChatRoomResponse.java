package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import lombok.Builder;

@Builder
public record ChatRoomResponse(
    Long chatRoomId,
    String name
) {

  public static ChatRoomResponse fromEntity(ChatRoom chatRoom) {
    return ChatRoomResponse.builder()
        .chatRoomId(chatRoom.getId())
        .name(chatRoom.getName())
        .build();
  }
}