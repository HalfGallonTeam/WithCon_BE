package com.halfgallon.withcon.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ChatRoomResponse(
    Long managerId,
    Long chatRoomId,
    String roomName,
    String performanceId,
    Integer userCount,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt
) {

  public static ChatRoomResponse fromEntity(ChatRoom chatRoom) {
    return ChatRoomResponse.builder()
        .chatRoomId(chatRoom.getId())
        .roomName(chatRoom.getName())
        .managerId(chatRoom.getManagerId())
        .performanceId(chatRoom.getPerformance().getId())
        .userCount(chatRoom.getUserCount())
        .createdAt(chatRoom.getCreatedAt())
        .build();
  }
}
