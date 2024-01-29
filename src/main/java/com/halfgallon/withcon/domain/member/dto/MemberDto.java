package com.halfgallon.withcon.domain.member.dto;

import com.halfgallon.withcon.domain.member.constant.LoginType;
import com.halfgallon.withcon.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberDto(
    String email,
    String username,
    String password,
    LoginType loginType,
    String nickName,
    String phoneNumber
) {

  public static MemberDto fromEntity(Member member) {
    return MemberDto.builder()
        .email(member.getEmail())
        .username(member.getUsername())
        .password(member.getPassword())
        .loginType(member.getLoginType())
        .nickName(member.getNickname())
        .phoneNumber(member.getPhoneNumber())
        .build();
  }
}
