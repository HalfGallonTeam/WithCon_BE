package com.halfgallon.withcon.domain.auth.client;

import static com.halfgallon.withcon.domain.member.constant.LoginType.KAKAO;
import static com.halfgallon.withcon.domain.member.constant.LoginType.NAVER;

import com.halfgallon.withcon.domain.auth.client.kakao.dto.response.KakaoUserInfoResponse;
import com.halfgallon.withcon.domain.auth.client.naver.dto.response.NaverUserInfoResponse;
import com.halfgallon.withcon.domain.member.constant.LoginType;
import com.halfgallon.withcon.domain.member.entity.Member;

public record OAuth2UserInfo(String snsId, String nickname, LoginType loginType) {

  public static OAuth2UserInfo ofNaver(NaverUserInfoResponse userInfoResponse) {
    return new OAuth2UserInfo(userInfoResponse.getResponse().getId(),
        userInfoResponse.getResponse().getNickname(), NAVER);
  }

  public static OAuth2UserInfo ofKakao(KakaoUserInfoResponse userInfoResponse) {
    return new OAuth2UserInfo(String.valueOf(userInfoResponse.getId()),
        userInfoResponse.getKakao_account().getProfile().getNickname(), KAKAO);
  }

  public Member toEntity() {
    return Member.builder()
        .username(this.snsId())
        .nickname(this.nickname())
        .loginType(this.loginType())
        .build();
  }
}
