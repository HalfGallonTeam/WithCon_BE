package com.halfgallon.withcon.domain.auth.controller;

import static com.halfgallon.withcon.domain.auth.constant.AuthConstant.ACCESS_TOKEN_HEADER_NAME;
import static com.halfgallon.withcon.domain.auth.constant.AuthConstant.ACCESS_TOKEN_PREFIX;
import static com.halfgallon.withcon.domain.auth.constant.AuthConstant.REFRESH_TOKEN_COOKIE_NAME;

import com.halfgallon.withcon.domain.auth.dto.request.AuthJoinRequest;
import com.halfgallon.withcon.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/join")
  public ResponseEntity<Void> join(@RequestBody @Valid AuthJoinRequest authJoinRequest) {
    authService.join(authJoinRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/username-duplication-check")
  public ResponseEntity<?> usernameDuplicationCheck(@RequestBody String username) {
    authService.usernameDuplicationCheck(username);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/phone-number-duplication-check")
  public ResponseEntity<?> phoneNumberDuplicationCheck(@RequestBody String phoneNumber) {
    authService.phoneNumberDuplicationCheck(phoneNumber);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/reissue")
  public ResponseEntity<Void> reissueAccessToken(
      @CookieValue(name = REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken) {
    String newAccessToken = authService.reissueAccessToken(refreshToken);

    HttpHeaders headers = new HttpHeaders();
    headers.add(ACCESS_TOKEN_HEADER_NAME, ACCESS_TOKEN_PREFIX + newAccessToken);

    return new ResponseEntity<>(headers, HttpStatus.OK);
  }
}
