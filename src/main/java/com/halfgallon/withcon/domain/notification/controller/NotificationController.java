package com.halfgallon.withcon.domain.notification.controller;

import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.service.NotificationService;
import com.halfgallon.withcon.domain.notification.service.SseEmitterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {

  private final SseEmitterService sseEmitterService;

  private final NotificationService notificationService;

  // 클라이언트가 알림을 구독
  @GetMapping(value ="/notification/subscribe", produces = "text/event-stream; charset=UTF-8")
  public ResponseEntity<SseEmitter> subscribe(
      @AuthenticationPrincipal UserDetails userDetails,
      @RequestHeader(value = "Last-Event-ID", required = false,
      defaultValue = "") String lastEventId) {

      return ResponseEntity.ok(
          sseEmitterService.subscribe(userDetails.getUsername(), lastEventId));
  }

  // 나에게 온 알림 조회
  @GetMapping("/notifications")
  public ResponseEntity<List<NotificationResponse>> findNotification(
      @RequestParam Long memberId) {

    return ResponseEntity.ok(notificationService.findNotification(memberId));
  }


}
