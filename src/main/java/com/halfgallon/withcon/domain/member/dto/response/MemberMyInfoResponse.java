package com.halfgallon.withcon.domain.member.dto.response;

import com.halfgallon.withcon.domain.member.constant.LoginType;
import com.halfgallon.withcon.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberMyInfoResponse(
    String username,
    String nickname,
    String phoneNumber,
    String profileImage,
    LoginType loginType
) {
가
  public static MemberMyInfoResponse fromEntity(Member member) {
    return MemberMyInfoResponse.builder()
        .username(member.getUsername())
        .nickname(member.getNickname())
        .phoneNumber(member.getPhoneNumber())
        .profileImage(member.getProfileImage())
        .loginType(member.getLoginType())
        .build();
  }
}
