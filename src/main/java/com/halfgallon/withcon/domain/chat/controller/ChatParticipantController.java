package com.halfgallon.withcon.domain.chat.controller;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.chat.dto.ChatParticipantResponse;
import com.halfgallon.withcon.domain.chat.service.ChatParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatParticipantController {

  private final ChatParticipantService chatParticipantService;

  @GetMapping("/chatRoom/member")
  public ResponseEntity<Page<ChatParticipantResponse>> findMyChatRoom(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PageableDefault(size = 5) Pageable pageable) {
    return ResponseEntity.ok(chatParticipantService.findMyChatRoom(customUserDetails, pageable));
  }

}
