package com.halfgallon.withcon.domain.auth.security.filter;

import static org.springframework.http.HttpMethod.POST;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfgallon.withcon.global.exception.CustomException;
import com.halfgallon.withcon.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

  private final ObjectMapper objectMapper;

  public LoginFilter(AntPathRequestMatcher antPathRequestMatcher,
      AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
    super(antPathRequestMatcher, authenticationManager);
    this.objectMapper = objectMapper;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response)
      throws AuthenticationException, IOException {
    if (!POST.name().equals(request.getMethod())) {
      throw new CustomException(ErrorCode.METHOD_NOT_SUPPORTED);
    }

    if (request.getContentType() == null || !request.getContentType()
        .equals(MediaType.APPLICATION_JSON_VALUE)) {
      throw new CustomException(ErrorCode.CONTENT_TYPE_NOT_SUPPORTED);
    }

    LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(),
        LoginRequest.class);

    String username = loginRequest.username;
    String password = loginRequest.password;

    UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(
        username, password);

    return this.getAuthenticationManager().authenticate(authRequest);
  }

  public record LoginRequest(String username, String password) {

  }
}
