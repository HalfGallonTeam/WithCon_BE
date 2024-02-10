package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import java.util.List;

public record ChatRoomRequest(
    String roomName,
    Long performanceId,
    List<String> tags
) {
  public ChatRoom toEntity() {
    return ChatRoom.builder()
        .name(this.roomName)
        .build();
  }
}
