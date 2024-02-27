package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.member.constant.LoginType;
import lombok.Builder;

@Builder
public record ChatParticipantDto(
    Long memberId,
    String password,
    String userProfile,
    LoginType loginType,
    String nickName,
    String phoneNumber
) {

  public static ChatParticipantDto fromEntity(ChatParticipant chatParticipant) {
    return ChatParticipantDto.builder()
        .memberId(chatParticipant.getMember().getId())
        .password(chatParticipant.getMember().getPassword())
        .userProfile(chatParticipant.getMember().getProfileImage())
        .loginType(chatParticipant.getMember().getLoginType())
        .nickName(chatParticipant.getMember().getNickname())
        .phoneNumber(chatParticipant.getMember().getPhoneNumber())
        .build();
  }
}
