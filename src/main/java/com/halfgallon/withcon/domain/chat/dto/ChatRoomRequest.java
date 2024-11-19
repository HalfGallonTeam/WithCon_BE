package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import java.util.List;

public record ChatRoomRequest(
    String roomName,
    String performanceId,
    List<String> tags
) {
  public ChatRoom toEntity(Long userId) {
    return ChatRoom.builder()
        .name(this.roomName)
        .managerId(userId)
        .build();
  }
}
