package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.member.constant.LoginType;
import lombok.Builder;

@Builder
public record ChatParticipantDto(
    String username,
    String password,
    LoginType loginType,
    String nickName,
    String phoneNumber
) {

  public static ChatParticipantDto fromEntity(ChatParticipant chatParticipant) {
    return ChatParticipantDto.builder()
        .username(chatParticipant.getMember().getUsername())
        .password(chatParticipant.getMember().getPassword())
        .loginType(chatParticipant.getMember().getLoginType())
        .nickName(chatParticipant.getMember().getNickname())
        .phoneNumber(chatParticipant.getMember().getPhoneNumber())
        .build();
  }
}
