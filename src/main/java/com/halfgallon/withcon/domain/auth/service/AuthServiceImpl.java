package com.halfgallon.withcon.domain.auth.service;

import com.halfgallon.withcon.domain.auth.dto.request.AuthJoinRequest;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.global.exception.CustomException;
import com.halfgallon.withcon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * 회원가입
   */
  @Override
  public void join(AuthJoinRequest request) {
    validateJoinRequest(request);

    String encodedPassword = passwordEncoder.encode(request.getPassword());

    memberRepository.save(request.toEntity(encodedPassword));
  }

  private void validateJoinRequest(AuthJoinRequest request) {
    if (memberRepository.existsByUsername(request.getUsername())) {
      throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
    }

    if (memberRepository.existsByEmail(request.getEmail())) {
      throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
    }

    if (memberRepository.existsByNickname(request.getNickname())) {
      throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
    }

    if (memberRepository.existsByPhoneNumber(request.getPhoneNumber())) {
      throw new CustomException(ErrorCode.DUPLICATE_PHONE_NUMBER);
    }
  }
}
