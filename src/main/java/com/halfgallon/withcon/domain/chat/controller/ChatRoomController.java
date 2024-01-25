package com.halfgallon.withcon.domain.chat.controller;

import com.halfgallon.withcon.domain.chat.dto.ChatRoomEnterResponse;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomResponse;
import com.halfgallon.withcon.domain.chat.service.ChatRoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  @PostMapping("/chatRoom")
  public ResponseEntity<ChatRoomResponse> createChatRoom(@RequestBody ChatRoomRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomService.createChatRoom(request));
  }

  @GetMapping("/chatRoom")
  public ResponseEntity<List<ChatRoomResponse>> findChatRoom() {
    return ResponseEntity.ok(chatRoomService.findChatRoom());
  }

  @GetMapping("/chatRoom/{chatRoomId}/enter")
  public ResponseEntity<ChatRoomEnterResponse> enterChatRoom(
      @PathVariable("chatRoomId") Long chatRoomId, @RequestParam Long memberId) {
    return ResponseEntity.ok(chatRoomService.enterChatRoom(chatRoomId, memberId));
  }

  @DeleteMapping("/chatRoom/{chatRoomId}/exit")
  public ResponseEntity<?> exitChatRoom(@PathVariable("chatRoomId") Long chatRoomId,
                                        @RequestParam Long memberId) {
    chatRoomService.exitChatRoom(chatRoomId, memberId);
    return ResponseEntity.noContent().build();
  }

}
