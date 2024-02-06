package com.halfgallon.withcon.domain.notification.dto.controller;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.notification.dto.ChatRoomNotificationRequest;
import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.dto.RedisChannelRequest;
import com.halfgallon.withcon.domain.notification.service.NotificationService;
import com.halfgallon.withcon.domain.notification.service.handler.ChatRoomRedisSubscriber;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  private final ChatRoomRedisSubscriber chatRoomRedisSubscriber;

  // 클라이언트가 알림을 구독
  @GetMapping(value = "/notification/subscribe", produces = "text/event-stream; charset=UTF-8")
  public ResponseEntity<SseEmitter> subscribe()
//      @AuthenticationPrincipal CustomUserDetails customUserDetails,
//      @RequestHeader(value = "Last-Event-ID", required = false,
//          defaultValue = "") String lastEventId) {
//
//    return ResponseEntity.ok(
//        notificationService.subscribe(customUserDetails.getId(), lastEventId));
  {
    Long memberId = 3L;
    String lastEventId = "5_5";
    return ResponseEntity.ok(notificationService.subscribe(memberId, lastEventId));
  }

  // 채팅방 관련 알림 생성 및 전송
  @PostMapping("/notification/event")
  public ResponseEntity<Void> createNotification(
      @RequestBody ChatRoomNotificationRequest request) {

    notificationService.createNotificationChatRoom(request);
    return ResponseEntity.ok().build();
  }

  // 채팅방 생성시 redis 구독
  @PostMapping("/notification/subscribe-channel")
  public ResponseEntity<Void> subscribeChatRoomChannel(
      @RequestBody RedisChannelRequest request) {
      chatRoomRedisSubscriber.
          subscribeChatRoomChannel(request.getPerformanceId(), request.getChatRoomId());
      return ResponseEntity.ok().build();
  }

  // 채팅방 폭파시 redis 해지
  @PostMapping("/notification/unsubscribe-channel")
  public ResponseEntity<Void> unsubscribeChatRoomChannel(
      @RequestBody RedisChannelRequest request) {
      chatRoomRedisSubscriber.
          unSubscribeChatRoomChannel(request.getPerformanceId(), request.getChatRoomId());
      return ResponseEntity.ok().build();
  }

  // 나에게 온 알림 조회
  @GetMapping("/notifications")
  public ResponseEntity<List<NotificationResponse>> findNotification(
//      @AuthenticationPrincipal CustomUserDetails customUserDetails)
      @RequestParam Long memberId) {

    return ResponseEntity.ok(notificationService.findNotification(memberId));
  }
}
