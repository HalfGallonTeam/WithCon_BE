package com.halfgallon.withcon.domain.auth.security.service;

import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.global.exception.CustomException;
import com.halfgallon.withcon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member findMember = memberRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return CustomUserDetails.fromEntity(findMember);
  }
}
