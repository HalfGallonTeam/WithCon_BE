package com.halfgallon.withcon.domain.auth.security.filter;

import static org.springframework.http.HttpMethod.POST;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfgallon.withcon.domain.auth.security.token.CustomOAuth2Token;
import com.halfgallon.withcon.global.exception.CustomException;
import com.halfgallon.withcon.global.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Slf4j
public class OAuth2LoginFilter extends AbstractAuthenticationProcessingFilter {

  private final ObjectMapper objectMapper;

  public OAuth2LoginFilter(
      RequestMatcher requiresAuthenticationRequestMatcher,
      AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
    super(requiresAuthenticationRequestMatcher, authenticationManager);
    this.objectMapper = objectMapper;
  }

  /**
   * POST "/auth/oauth2/login", { registrationId(e.g "naver", "kakao"), authorizationCode(인가 코드) }
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    //

    if (!POST.name().equals(request.getMethod())) {
      throw new CustomException(ErrorCode.METHOD_NOT_SUPPORTED);
    }

    if (request.getContentType() == null || !request.getContentType()
        .equals(MediaType.APPLICATION_JSON_VALUE)) {
      throw new CustomException(ErrorCode.CONTENT_TYPE_NOT_SUPPORTED);
    }

    OAuth2LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(),
        OAuth2LoginRequest.class);

    String registrationId = loginRequest.registrationId();
    String authorizationCode = loginRequest.authorizationCode();

    CustomOAuth2Token token = new CustomOAuth2Token(registrationId, authorizationCode);

    return getAuthenticationManager().authenticate(token);
  }

  public record OAuth2LoginRequest(String registrationId, String authorizationCode) {

  }
}
