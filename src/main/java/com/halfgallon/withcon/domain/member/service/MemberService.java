package com.halfgallon.withcon.domain.member.service;

import com.halfgallon.withcon.domain.member.dto.request.UpdateMemberRequest;
import com.halfgallon.withcon.domain.member.dto.response.MemberMyInfoResponse;

public interface MemberService {

  /**
   * 회원 마이페이지 조회
   */
  MemberMyInfoResponse getMyInformation(Long memberId);

  /**
   * 회원 수정
   */
  void updateMember(Long memberId, UpdateMemberRequest request);

  /**
   * 회원 탈퇴
   */
  void deleteMember(Long memberId);
}
