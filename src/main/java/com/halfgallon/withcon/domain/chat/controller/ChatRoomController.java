package com.halfgallon.withcon.domain.chat.controller;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.chat.dto.ChatMessageRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomEnterResponse;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomResponse;
import com.halfgallon.withcon.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  @PostMapping("/chatRoom")
  public ResponseEntity<ChatRoomResponse> createChatRoom(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestBody ChatRoomRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(
        chatRoomService.createChatRoom(customUserDetails ,request));
  }

  @GetMapping("/chatRoom/performance/{performanceId}")
  public ResponseEntity<Page<ChatRoomResponse>> findChatRoom(
      @PathVariable("performanceId") String performanceId,
      @PageableDefault(size = 5, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(chatRoomService.findChatRoom(performanceId, pageable));
  }

  @GetMapping("/chatRoom/{chatRoomId}/enter")
  public ResponseEntity<ChatRoomEnterResponse> enterChatRoom(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable("chatRoomId") Long chatRoomId) {
    return ResponseEntity.ok(chatRoomService.enterChatRoom(customUserDetails, chatRoomId));
  }

  @DeleteMapping("/chatRoom/{chatRoomId}/exit")
  public ResponseEntity<?> exitChatRoom(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                        @PathVariable("chatRoomId") Long chatRoomId) {
    chatRoomService.exitChatRoom(customUserDetails, chatRoomId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/chatRoom/{chatRoomId}/kick/{memberId}")
  public ResponseEntity<?> kickChatRoom(@AuthenticationPrincipal CustomUserDetails customUserDetails,
      @PathVariable("chatRoomId") Long chatRoomId,
      @PathVariable("memberId") Long memberId) {
    return ResponseEntity.ok(chatRoomService.kickChatRoom(customUserDetails, chatRoomId, memberId));
  }

  @GetMapping("/chatRoom/{chatRoomId}/message")
  public ResponseEntity<?> findAllMessageChatRoom(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @ModelAttribute ChatMessageRequest chatMessageRequest,
      @PathVariable("chatRoomId") Long chatRoomId) {
    return ResponseEntity.ok(
        chatRoomService.findAllMessageChatRoom(customUserDetails, chatMessageRequest, chatRoomId));
  }
}
