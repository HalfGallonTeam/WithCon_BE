package com.halfgallon.withcon.domain.notification.event.handler;

import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.domain.notification.constant.Channel;
import com.halfgallon.withcon.domain.notification.constant.NotificationMessage;
import com.halfgallon.withcon.domain.notification.constant.NotificationType;
import com.halfgallon.withcon.domain.notification.constant.RedisCacheType;
import com.halfgallon.withcon.domain.notification.constant.VisibleType;
import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.dto.VisibleDataDto;
import com.halfgallon.withcon.domain.notification.entity.Notification;
import com.halfgallon.withcon.domain.notification.event.NewMessageEvent;
import com.halfgallon.withcon.domain.notification.repository.NotificationRepository;
import com.halfgallon.withcon.domain.notification.service.RedisCacheService;
import com.halfgallon.withcon.domain.notification.service.RedisNotificationService;
import com.halfgallon.withcon.global.exception.CustomException;
import com.halfgallon.withcon.global.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
  @Transactional
  public void handleNewMessageEvent(NewMessageEvent event) {
    String visibleKey = RedisCacheType.VISIBLE_CACHE.getDescription()
        + Channel.makeChannel(event.getPerformanceId(), event.getChatRoomId());
    log.info("Event Handle : 채널 KEY: " + visibleKey);

    Map<Object, Object> cache = redisCacheService.getHashByKey(visibleKey);
    log.info("Event : Visible 캐시 데이터 조회" + cache);

    if(!cache.isEmpty()) { // 기존 캐시 값이 존재 한다면
      Iterator<Entry<Object, Object>> iterator = cache.entrySet().iterator();

      while (iterator.hasNext()) {
        Map.Entry<Object, Object> entry = iterator.next();
        VisibleDataDto visibleDataDto = (VisibleDataDto) entry.getValue();

        if (visibleDataDto.getVisibleType() == VisibleType.HIDDEN) {
          Member member = memberRepository.findById(Long.parseLong((String)entry.getKey()))
              .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

          newMessageSaveAndPublish(event, member, visibleKey);

          iterator.remove();
          log.info("캐시 데이터 삭제: " + entry.getKey());
        }
      }
    }
  }
  private void newMessageSaveAndPublish(NewMessageEvent event, Member member, String visibleKey) {
    Notification notification = Notification.builder()
        .message(createNewMessageNotification())
        .url(createNewMessageUrl(event.getChatRoomId()))
        .notificationType(NotificationType.CHATROOM)
        .createdAt(LocalDateTime.now())
        .member(member)
        .build();

    notificationRepository.save(notification);
    log.info("Event : 알림 저장 성공 ");

    redisNotificationService.publish(visibleKey, new NotificationResponse(notification));
    log.info("Event : 메세지 발행 ");
  }

  private String createNewMessageUrl(Long chatRoomId) {
    return NotificationType.CHATROOM.getDescription() + "/" + chatRoomId + "/enter";
  }

  private String createNewMessageNotification() {
    return NotificationMessage.NEW_MESSAGE_FROM_CHATROOM.getDescription();
  }
}
