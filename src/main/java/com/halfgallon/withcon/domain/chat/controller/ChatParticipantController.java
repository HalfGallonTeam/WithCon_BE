package com.halfgallon.withcon.domain.chat.controller;

import com.halfgallon.withcon.domain.chat.dto.ChatParticipantResponse;
import com.halfgallon.withcon.domain.chat.service.ChatParticipantService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatParticipantController {

  private final ChatParticipantService chatParticipantService;

  @GetMapping("/chatRoom/members/{memberId}")
  public ResponseEntity<List<ChatParticipantResponse>> findMyChatRoom(
      @PathVariable("memberId") Long memberId) {
    return ResponseEntity.ok(chatParticipantService.findMyChatRoom(memberId));
  }

}
