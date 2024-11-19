package com.halfgallon.withcon.domain.notification.controller;

import com.halfgallon.withcon.domain.notification.service.handler.ChatRoomRedisSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class RedisSubscribeController {

  private final ChatRoomRedisSubscriber chatRoomRedisSubscriber;

  // 채팅방 생성시 redis 구독
  @PostMapping("/subscribe-channel")
  public ResponseEntity<Void> subscribeChatRoomChannel(
      @RequestParam Long chatRoomId) {
    chatRoomRedisSubscriber.subscribeChatRoomChannel(chatRoomId);
    return ResponseEntity.ok().build();
  }

  // 채팅방 폭파시 redis 해지
  @PostMapping("/unsubscribe-channel")
  public ResponseEntity<Void> unsubscribeChatRoomChannel(
      @RequestParam Long chatRoomId) {
    chatRoomRedisSubscriber.unSubscribeChatRoomChannel(chatRoomId);
    return ResponseEntity.ok().build();
  }
}
