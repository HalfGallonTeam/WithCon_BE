package com.halfgallon.withcon.domain.member.service.impl;

import static com.halfgallon.withcon.domain.member.constant.LoginType.HOME;
import static com.halfgallon.withcon.global.exception.ErrorCode.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.halfgallon.withcon.domain.member.dto.request.UpdateMemberRequest;
import com.halfgallon.withcon.domain.member.dto.response.MemberMyInfoResponse;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.global.exception.CustomException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

  @Mock
  private MemberRepository memberRepository;

  @InjectMocks
  private MemberServiceImpl memberService;

  @Test
  @DisplayName("❗️내 정보 조회 시 없는 회원이면 실패한다.")
  void getMyInformation_Fail_Member_Not_Found() {
    // given
    Long memberId = 1L;

    given(memberRepository.findById(memberId)).willReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> memberService.getMyInformation(memberId))
        .isInstanceOf(CustomException.class)
        .hasMessageContaining(MEMBER_NOT_FOUND.getDescription());
  }

  @Test
  @DisplayName("내 정보 조회 성공")
  void getMyInformation_Success() {
    // given
    Long memberId = 1L;
    Member findMember = Member.builder()
        .id(memberId)
        .username("username")
        .nickname("위드콘")
        .phoneNumber("010-1234-5678")
        .profileImage("image-url")
        .loginType(HOME)
        .build();

    given(memberRepository.findById(memberId)).willReturn(Optional.of(findMember));

    // when
    MemberMyInfoResponse response = memberService.getMyInformation(memberId);

    // then
    assertThat(response.memberId()).isEqualTo(1L);
    assertThat(response.username()).isEqualTo("username");
    assertThat(response.nickname()).isEqualTo("위드콘");
    assertThat(response.phoneNumber()).isEqualTo("010-1234-5678");
    assertThat(response.profileImage()).isEqualTo("image-url");
    assertThat(response.loginType()).isEqualTo(HOME);
  }

  @Test
  @DisplayName("❗️내 정보 변경 시 없는 회원이면 실패한다.")
  void updateMember_Fail_Member_Not_Found() {
    // given
    Long memberId = 1L;

    UpdateMemberRequest request =
        new UpdateMemberRequest("변경위드콘", "010-9876-5432");

    given(memberRepository.findById(memberId)).willReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> memberService.updateMember(memberId, request))
        .isInstanceOf(CustomException.class)
        .hasMessageContaining(MEMBER_NOT_FOUND.getDescription());
  }

  @Test
  @DisplayName("내 정보 변경 성공")
  void updateMember_Success() {
    // given
    Long memberId = 1L;

    UpdateMemberRequest request =
        new UpdateMemberRequest("변경위드콘", "010-9876-5432");

    Member findMember = Member.builder()
        .id(memberId)
        .nickname("위드콘")
        .phoneNumber("010-1234-5678")
        .loginType(HOME)
        .build();

    given(memberRepository.findById(memberId)).willReturn(Optional.of(findMember));

    // when
    memberService.updateMember(memberId, request);

    // then
    assertThat(findMember.getNickname()).isEqualTo("변경위드콘");
    assertThat(findMember.getPhoneNumber()).isEqualTo("010-9876-5432");
  }

  @Test
  @DisplayName("❗회원탈퇴 시 없는 회원이면 실패한다.")
  void deleteMember_Fail_Member_Not_Found() {
    // given
    Long memberId = 1L;

    given(memberRepository.findById(memberId)).willReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> memberService.deleteMember(memberId))
        .isInstanceOf(CustomException.class)
        .hasMessageContaining(MEMBER_NOT_FOUND.getDescription());
  }

  @Test
  @DisplayName("회원 탈퇴 성공")
  void deleteMember_Success() {
    // given
    Long memberId = 1L;

    Member findMember = Member.builder()
        .id(memberId)
        .nickname("위드콘")
        .phoneNumber("010-1234-5678")
        .loginType(HOME)
        .build();

    given(memberRepository.findById(memberId)).willReturn(Optional.of(findMember));

    // when
    memberService.deleteMember(memberId);

    // then
    verify(memberRepository).delete(findMember);
  }
}