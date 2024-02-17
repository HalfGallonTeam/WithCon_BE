package com.halfgallon.withcon.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import com.halfgallon.withcon.domain.tag.entity.Tag;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record ChatRoomResponse(
    Long chatRoomId,
    String roomName,
    String managerName,
    Long performanceId,
    Integer userCount,
    List<String> tags,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt
) {

  public static ChatRoomResponse fromEntity(ChatRoom chatRoom) {
    return ChatRoomResponse.builder()
        .chatRoomId(chatRoom.getId())
        .roomName(chatRoom.getName())
        .managerName(chatRoom.getManagerName())
        .performanceId(Long.valueOf(chatRoom.getPerformance().getId()))
        .userCount(chatRoom.getUserCount())
        .tags(chatRoom.getTags().stream().map(Tag::getName).toList())
        .createdAt(chatRoom.getCreatedAt())
        .build();
  }
}
