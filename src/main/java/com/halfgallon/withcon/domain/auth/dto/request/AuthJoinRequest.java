package com.halfgallon.withcon.domain.auth.dto.request;

import com.halfgallon.withcon.domain.member.constant.LoginType;
import com.halfgallon.withcon.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthJoinRequest {

  @NotBlank
  @Pattern(
      regexp = "^[a-zA-Z0-9]{2,12}$",
      message = "ID 형식이 올바르지 않습니다."
  )
  private String username;

  @NotBlank
  @Pattern(
      regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%?&])[A-Za-z\\d@$!%*?&]{8,12}$",
      message = "비밀번호 형식이 올바르지 않습니다."
  )
  private String password;

  @NotBlank
  @Pattern(
      regexp = "^[a-zA-Z0-9._%+-]+@[a-z0-9.-]+.[a-zA-Z]{2,}$",
      message = "이메일 형식이 올바르지 않습니다."
  )
  private String email;

  @NotBlank
  @Pattern(
      regexp = "^[A-Za-z0-9ㄱ-힣]{2,6}$",
      message = "닉네임 형식이 올바르지 않습니다."
  )
  private String nickname;

  @NotBlank
  private String phoneNumber;

  public Member toEntity(String encodedPassword) {
    return Member.builder()
        .username(username)
        .password(encodedPassword)
        .nickname(nickname)
        .email(email)
        .phoneNumber(phoneNumber)
        .loginType(LoginType.HOME)
        .build();
  }
}
