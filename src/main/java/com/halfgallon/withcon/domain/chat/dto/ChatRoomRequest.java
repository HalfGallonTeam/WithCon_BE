package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.entity.ChatRoom;

public record ChatRoomRequest(
    String roomName,
    String performanceId
) {
  public ChatRoom toEntity(Long userId) {
    return ChatRoom.builder()
        .name(this.roomName)
        .managerId(userId)
        .build();
  }
}
