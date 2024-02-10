package com.halfgallon.withcon.domain.member.service.impl;

import static com.halfgallon.withcon.global.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.halfgallon.withcon.domain.member.dto.request.UpdateMemberRequest;
import com.halfgallon.withcon.domain.member.dto.response.MemberMyInfoResponse;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.domain.member.service.MemberService;
import com.halfgallon.withcon.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;

  @Override
  public MemberMyInfoResponse getMyInformation(Long memberId) {
    Member findMember = findMemberOrThrow(memberId);
    return MemberMyInfoResponse.fromEntity(findMember);
  }

  @Override
  @Transactional
  public void updateMember(Long memberId, UpdateMemberRequest request) {
    Member findMember = findMemberOrThrow(memberId);
    findMember.update(request);
  }

  @Override
  @Transactional
  public void deleteMember(Long memberId) {
    Member findMember = findMemberOrThrow(memberId);
    memberRepository.delete(findMember);
  }

  private Member findMemberOrThrow(Long memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
  }
}