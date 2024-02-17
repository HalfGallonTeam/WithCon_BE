package com.halfgallon.withcon.domain.auth.service;

import static com.halfgallon.withcon.global.exception.ErrorCode.DUPLICATE_PHONE_NUMBER;
import static com.halfgallon.withcon.global.exception.ErrorCode.DUPLICATE_USERNAME;
import static com.halfgallon.withcon.global.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.halfgallon.withcon.global.exception.ErrorCode.REFRESH_TOKEN_COOKIE_IS_EMPTY;
import static com.halfgallon.withcon.global.exception.ErrorCode.REFRESH_TOKEN_EXPIRED;

import com.halfgallon.withcon.domain.auth.dto.request.AuthJoinRequest;
import com.halfgallon.withcon.domain.auth.entity.AccessToken;
import com.halfgallon.withcon.domain.auth.entity.RefreshToken;
import com.halfgallon.withcon.domain.auth.manager.JwtManager;
import com.halfgallon.withcon.domain.auth.repository.AccessTokenRepository;
import com.halfgallon.withcon.domain.auth.repository.RefreshTokenRepository;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final JwtManager jwtManager;
  private final PasswordEncoder passwordEncoder;
  private final MemberRepository memberRepository;
  private final AccessTokenRepository accessTokenRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  /**
   * 회원가입
   */
  @Override
  public void join(AuthJoinRequest request) {
    usernameDuplicationValidate(request.getUsername());

    String encodedPassword = passwordEncoder.encode(request.getPassword());

    memberRepository.save(request.toEntity(encodedPassword));
  }

  /**
   * username 중복 체크
   */
  @Override
  public void usernameDuplicationCheck(String username) {
    usernameDuplicationValidate(username);
  }

  private void usernameDuplicationValidate(String username) {
    if (memberRepository.existsByUsername(username)) {
      throw new CustomException(DUPLICATE_USERNAME);
    }
  }

  /**
   * phoneNumber 중복 체크
   */
  @Override
  public void phoneNumberDuplicationCheck(String phoneNumber) {
    if (memberRepository.existsByPhoneNumber(phoneNumber)) {
      throw new CustomException(DUPLICATE_PHONE_NUMBER);
    }
  }

  /**
   * 액세스 토큰 재발급
   */
  @Override
  public String reissueAccessToken(String refreshToken) {
    if (!StringUtils.hasText(refreshToken)) {
      throw new CustomException(REFRESH_TOKEN_COOKIE_IS_EMPTY);
    }

    RefreshToken findRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new CustomException(REFRESH_TOKEN_EXPIRED));

    Long memberId = findRefreshToken.getMemberId();

    if (memberRepository.findById(memberId).isEmpty()) {
      throw new CustomException(MEMBER_NOT_FOUND);
    }

    String newAccessToken = jwtManager.createAccessToken(memberId);

    accessTokenRepository.save(new AccessToken(memberId, newAccessToken));

    return newAccessToken;
  }
}
