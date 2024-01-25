package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import lombok.Builder;

@Builder
public record ChatParticipantResponse(
    Long memberId,
    Long chatRoomId,
    String chatRoomName
) {

  public static ChatParticipantResponse fromEntity(ChatParticipant participant) {
    return ChatParticipantResponse.builder()
        .memberId(participant.getMember().getId())
        .chatRoomId(participant.getChatRoom().getId())
        .chatRoomName(participant.getChatRoom().getName())
        .build();
  }

}
