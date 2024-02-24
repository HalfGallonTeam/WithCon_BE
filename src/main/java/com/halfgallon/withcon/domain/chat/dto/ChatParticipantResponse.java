package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.tag.entity.Tag;
import java.util.List;
import lombok.Builder;

@Builder
public record ChatParticipantResponse(
    Long memberId,
    Long chatRoomId,
    String chatRoomName,
    List<String> tags
) {

  public static ChatParticipantResponse fromEntity(ChatParticipant participant) {
    return ChatParticipantResponse.builder()
        .memberId(participant.getMember().getId())
        .chatRoomId(participant.getChatRoom().getId())
        .chatRoomName(participant.getChatRoom().getName())
        .tags(participant.getChatRoom().getTags().stream().map(Tag::getName).toList())
        .build();
  }

}
