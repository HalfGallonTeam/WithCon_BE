package com.halfgallon.withcon.domain.notification.event.handler;

import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.domain.notification.constant.NotificationMessage;
import com.halfgallon.withcon.domain.notification.constant.NotificationType;
import com.halfgallon.withcon.domain.notification.constant.RedisCacheType;
import com.halfgallon.withcon.domain.notification.constant.VisibleType;
import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.entity.Notification;
import com.halfgallon.withcon.domain.notification.event.NewMessageEvent;
import com.halfgallon.withcon.domain.notification.repository.NotificationRepository;
import com.halfgallon.withcon.domain.notification.service.RedisCacheService;
import com.halfgallon.withcon.domain.notification.service.RedisNotificationService;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewMessageEventHandler {

  private final RedisNotificationService redisNotificationService;
  private final RedisCacheService redisCacheService;
  private final MemberRepository memberRepository;
  private final NotificationRepository notificationRepository;

  @Async
  @EventListener
  public void handleNewMessageEvent(NewMessageEvent event) {
    String visibleKey = RedisCacheType.VISIBLE_CACHE.getDescription()
        + event.getChatRoomId();
    log.info("Event Handle : 채널 KEY: " + visibleKey);

    Map<Object, Object> cache = redisCacheService.getHashByKey(visibleKey);
    log.info("Event : Visible 캐시 데이터 조회" + cache);

    if(cache != null) { // 기존 캐시 Map이 존재 한다면
      for(Map.Entry<Object, Object> entry : cache.entrySet()) {
        VisibleType visibleType = VisibleType.valueOf((String)entry.getValue());

        if(visibleType == VisibleType.HIDDEN) {
          memberRepository.findById(Long.parseLong((String)entry.getKey()))
              .ifPresent(member -> {
                newMessageSaveAndPublish(event, member);
                cache.put(entry.getKey(), VisibleType.NONE);
              });
        }
      }
      redisCacheService.saveToHash(visibleKey, cache, 24);
    }
  }
  private void newMessageSaveAndPublish(NewMessageEvent event, Member member) {
    Notification notification = Notification.builder()
        .message(createNewMessageNotification())
        .url(createNewMessageUrl(event.getChatRoomId()))
        .notificationType(NotificationType.CHATROOM)
        .createdAt(LocalDateTime.now())
        .member(member)
        .build();

    notificationRepository.save(notification);
    log.info("Event : 알림 저장 성공 ");

    redisNotificationService.publish(String.valueOf(member.getId()), new NotificationResponse(notification));
    log.info("Event : 메세지 발행 ");
  }

  private String createNewMessageUrl(Long chatRoomId) {
    return NotificationType.CHATROOM.getDescription() + "/" + chatRoomId + "/enter";
  }

  private String createNewMessageNotification() {
    return NotificationMessage.NEW_MESSAGE_FROM_CHATROOM.getDescription();
  }
}
