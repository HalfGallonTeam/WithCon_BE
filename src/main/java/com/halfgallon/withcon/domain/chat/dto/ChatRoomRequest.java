package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.entity.ChatRoom;

public record ChatRoomRequest(
    Long memberId,
    String name
) {

  public ChatRoom toEntity() {
    return ChatRoom.builder()
        .name(this.name)
        .build();
  }
}
