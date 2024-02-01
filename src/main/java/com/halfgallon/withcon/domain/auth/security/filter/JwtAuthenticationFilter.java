package com.halfgallon.withcon.domain.auth.security.filter;

import static com.halfgallon.withcon.domain.auth.constant.AuthConstant.ACCESS_TOKEN_HEADER_NAME;
import static com.halfgallon.withcon.domain.auth.constant.AuthConstant.ACCESS_TOKEN_PREFIX;

import com.halfgallon.withcon.domain.auth.entity.AccessToken;
import com.halfgallon.withcon.domain.auth.repository.AccessTokenRepository;
import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.global.exception.CustomException;
import com.halfgallon.withcon.global.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final MemberRepository memberRepository;
  private final AccessTokenRepository accessTokenRepository;

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String accessTokenHeader = request.getHeader(ACCESS_TOKEN_HEADER_NAME);

    if (!StringUtils.hasText(accessTokenHeader)) {
      filterChain.doFilter(request, response);
      return;
    }

    if (!accessTokenHeader.startsWith(ACCESS_TOKEN_PREFIX)) {
      throw new CustomException(ErrorCode.ACCESS_TOKEN_NOT_SUPPORTED);
    }

    String accessToken = accessTokenHeader.substring(ACCESS_TOKEN_PREFIX.length());

    AccessToken findAccessToken = accessTokenRepository.findByAccessToken(accessToken)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCESS_TOKEN_EXPIRED));

    Long memberId = findAccessToken.getMemberId();

    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    authenticate(member);
    filterChain.doFilter(request, response);
  }

  private void authenticate(Member member) {
    CustomUserDetails principal = CustomUserDetails.fromEntity(member);
    Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "",
        principal.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
