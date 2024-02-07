package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import java.util.List;

public record ChatRoomRequest(
    String name,
    List<String> tags
) {
  public ChatRoom toEntity() {
    return ChatRoom.builder()
        .name(this.name)
        .build();
  }
}
