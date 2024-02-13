package com.halfgallon.withcon.domain.notification.controller;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.notification.dto.ChatRoomNotificationRequest;
import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.dto.RedisChannelRequest;
import com.halfgallon.withcon.domain.notification.dto.VisibleRequest;
import com.halfgallon.withcon.domain.notification.service.NotificationService;
import com.halfgallon.withcon.domain.notification.service.RedisNotificationService;
import com.halfgallon.withcon.domain.notification.service.handler.ChatRoomRedisSubscriber;
import com.halfgallon.withcon.domain.notification.service.handler.GenerateEvent;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;
  private final RedisNotificationService redisNotificationService;
  private final GenerateEvent event;

  private final ChatRoomRedisSubscriber chatRoomRedisSubscriber;

  // 클라이언트가 알림을 구독
  @PostMapping(value = "/notification/subscribe", produces = "text/event-stream; charset=UTF-8")
  public ResponseEntity<SseEmitter> subscribe(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestHeader(value = "Last-Event-ID", required = false,
          defaultValue = "") String lastEventId) {

    return ResponseEntity.ok(
        notificationService.subscribe(customUserDetails.getId(), lastEventId));
  }

  // 채팅방 관련 알림 생성 및 전송
  @PostMapping("/notification/chatRoom-event")
  public ResponseEntity<Void> createNotification(
      @RequestBody @Valid ChatRoomNotificationRequest request) {

    notificationService.createNotificationChatRoom(request);
    return ResponseEntity.ok().build();
  }

  // 채팅방 생성시 redis 구독
  @PostMapping("/notification/subscribe-channel")
  public ResponseEntity<Void> subscribeChatRoomChannel(
      @RequestBody @Valid RedisChannelRequest request) {
      chatRoomRedisSubscriber.
          subscribeChatRoomChannel(request.getPerformanceId(), request.getChatRoomId());
      return ResponseEntity.ok().build();
  }

  // 채팅방 폭파시 redis 해지
  @PostMapping("/notification/unsubscribe-channel")
  public ResponseEntity<Void> unsubscribeChatRoomChannel(
      @RequestBody @Valid RedisChannelRequest request) {
      chatRoomRedisSubscriber.
          unSubscribeChatRoomChannel(request.getPerformanceId(), request.getChatRoomId());
      return ResponseEntity.ok().build();
  }

  // 나에게 온 알림 조회
  @GetMapping("/notifications")
  public ResponseEntity<List<NotificationResponse>> findNotification(
      @AuthenticationPrincipal CustomUserDetails customUserDetails) {

    return ResponseEntity.ok(
        notificationService.findNotification(customUserDetails.getId()));
  }

  // 회원이 채팅방을 보고 있는지 여부 전달
  @PostMapping("/notification/visible")
  public ResponseEntity<Void> visibleChatRoom(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestBody @Valid VisibleRequest request) {

    redisNotificationService.createVisibleCache(
        customUserDetails.getId(), request);
    return ResponseEntity.ok().build();
  }

  // 채팅방 visible 이슈가 발생
  @PostMapping("/notification/event")
  public ResponseEntity<Void> generateEvent(
      @RequestBody @Valid RedisChannelRequest request) {

    event.doSomething(request.getPerformanceId(), request.getChatRoomId());
      return ResponseEntity.ok().build();
  }
}
