package com.halfgallon.withcon.global.annotation;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {

  @Override
  public SecurityContext createSecurityContext(WithCustomMockUser member) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    CustomUserDetails principal = new CustomUserDetails(member.id(), member.username(),
        member.password());
    Authentication authentication = new TestingAuthenticationToken(principal, "", "ROLE_USER");
    context.setAuthentication(authentication);
    return context;
  }
}
