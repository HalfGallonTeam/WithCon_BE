package com.halfgallon.withcon.domain.auth.controller;

import com.halfgallon.withcon.domain.auth.dto.request.AuthJoinRequest;
import com.halfgallon.withcon.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/auth/join")
  public ResponseEntity<Void> join(@RequestBody @Valid AuthJoinRequest authJoinRequest) {
    authService.join(authJoinRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
