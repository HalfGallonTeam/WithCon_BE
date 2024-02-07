package com.halfgallon.withcon.domain.auth.security.handler;

import static com.halfgallon.withcon.global.exception.ErrorCode.OAUTH2_LOGIN_FAILURE_MESSAGE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halfgallon.withcon.global.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Slf4j
@RequiredArgsConstructor
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    log.info("소셜 로그인 실패");

    ErrorResponse errorResponse = new ErrorResponse(BAD_REQUEST.value(),
        OAUTH2_LOGIN_FAILURE_MESSAGE,
        OAUTH2_LOGIN_FAILURE_MESSAGE.getDescription());

    response.setContentType(APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(UTF_8.name());
    response.setStatus(BAD_REQUEST.value());

    objectMapper.writeValue(response.getWriter(), errorResponse);
  }
}
