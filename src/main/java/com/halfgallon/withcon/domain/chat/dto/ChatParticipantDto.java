package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import lombok.Builder;

@Builder
public record ChatParticipantDto(
    Long memberId,
    String password,
    String nickName,
    String phoneNumber
) {

  public static ChatParticipantDto fromEntity(ChatParticipant chatParticipant) {
    return ChatParticipantDto.builder()
        .memberId(chatParticipant.getMember().getId())
        .password(chatParticipant.getMember().getPassword())
        .nickName(chatParticipant.getMember().getNickname())
        .phoneNumber(chatParticipant.getMember().getPhoneNumber())
        .build();
  }
}
