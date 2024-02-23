package com.halfgallon.withcon.domain.member.service;

import com.halfgallon.withcon.domain.member.dto.request.UpdateMemberRequest;
import com.halfgallon.withcon.domain.member.dto.response.MemberMyInfoResponse;
import org.springframework.web.multipart.MultipartFile;

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
   * 현재 비밀번호 일치 확인
   */
  void currentPasswordCheck(Long memberId, String password);

  /**
   * 회원 프로필 사진 업로드
   */
  void uploadProfileImage(Long memberId, MultipartFile image);

  /**
   * 회원 탈퇴
   */
  void deleteMember(Long memberId);

}
