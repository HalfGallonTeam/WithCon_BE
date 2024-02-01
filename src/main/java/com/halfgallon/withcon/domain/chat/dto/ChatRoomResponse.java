package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import com.halfgallon.withcon.domain.tag.dto.TagDto;
import java.util.List;
import lombok.Builder;

@Builder
public record ChatRoomResponse(
    Long chatRoomId,
    String name,
    Integer userCount,
    List<TagDto> tagList
) {

  public static ChatRoomResponse fromEntity(ChatRoom chatRoom) {
    return ChatRoomResponse.builder()
        .chatRoomId(chatRoom.getId())
        .name(chatRoom.getName())
        .userCount(chatRoom.getUserCount())
        .tagList(chatRoom.getTags().stream().map(TagDto::fromEntity).toList())
        .build();
  }
}
