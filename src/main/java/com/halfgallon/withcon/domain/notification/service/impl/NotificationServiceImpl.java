package com.halfgallon.withcon.domain.notification.service.impl;

import com.halfgallon.withcon.domain.chat.constant.MessageType;
import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.chat.repository.ChatParticipantRepository;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.domain.notification.constant.Channel;
import com.halfgallon.withcon.domain.notification.constant.NotificationMessage;
import com.halfgallon.withcon.domain.notification.constant.NotificationType;
import com.halfgallon.withcon.domain.notification.constant.RedisCacheType;
import com.halfgallon.withcon.domain.notification.dto.ChatRoomNotificationRequest;
import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.entity.Notification;
import com.halfgallon.withcon.domain.notification.repository.NotificationRepository;
import com.halfgallon.withcon.domain.notification.repository.SseEmitterRepository;
import com.halfgallon.withcon.domain.notification.service.NotificationService;
import com.halfgallon.withcon.domain.notification.service.RedisCacheService;
import com.halfgallon.withcon.domain.notification.service.RedisNotificationService;
import com.halfgallon.withcon.domain.notification.service.SseEmitterService;
import com.halfgallon.withcon.global.exception.CustomException;
import com.halfgallon.withcon.global.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private static final long TIME_OUT = 30L * 1000 * 60;

  private final NotificationRepository notificationRepository;
  private final SseEmitterRepository sseEmitterRepository;
  private final MemberRepository memberRepository;
  private final ChatParticipantRepository chatParticipantRepository;

  private final SseEmitterService sseEmitterService;
  private final RedisNotificationService redisNotificationService;
  private final RedisCacheService redisCacheService;

  @Override
  public SseEmitter subscribe(Long memberId, String lastEventId) {
    String emitterId = createEmitterId(memberId);
    SseEmitter sseEmitter = sseEmitterRepository.save(emitterId, new SseEmitter(TIME_OUT));

    // 더미 데이터(503 에러 방지)
    sseEmitterService.send(sseEmitter, emitterId,
        NotificationMessage.SUBSCRIBE.getDescription() + " memberId: " + memberId);
    log.info("SSE 구독 완료");

    redisNotificationService.subscribe(emitterId);

    sseEmitter.onCompletion(() -> {
      log.info("onCompletion callback");
      sseEmitterRepository.deleteById(emitterId);
      redisNotificationService.unsubscribe(emitterId);
    });
    sseEmitter.onError((e) -> {
      log.info("onError callback");
      sseEmitter.complete();
    });
    sseEmitter.onTimeout(() -> {
      log.info("onTimeout callback");
      sseEmitterRepository.deleteById(emitterId);
      redisNotificationService.unsubscribe(emitterId);
    });

    if (StringUtils.hasText(lastEventId)) { // true -> 있으면 유실 데이터 존재
      sendLostNotification(sseEmitter, lastEventId);
    }

    return sseEmitter;
  }

  private String createEmitterId(Long memberId) {
    return memberId + "_" + System.currentTimeMillis();
  }

  /**
   * 예시 마지막으로 받은 emitterId 값인 last-event-id = 3_3556 인 경우 redis 저장소에서 뒷 자리가 3556보다 큰 emitterId key를
   * 가진 value는 redis에 저장만 되고 전송은 실패했다. 따라서 last-event-id 보다 큰 key에 해당하는 value를 보내준다.
   */
  private void sendLostNotification(SseEmitter sseEmitter, String lastEventId) {
    String[] parts = lastEventId.split("_");
    String hashKey = RedisCacheType.NOTIFICATION_CACHE.getDescription() + parts[0];

    Map<Object, Object> cache = redisCacheService.getHashByKey(hashKey);
    log.info("Service : 캐시 리스트 조회: " + cache);

    if(cache == null || cache.isEmpty()) {
      log.info("Service : 캐시 데이터가 없음.");
      return;
    }

    cache.entrySet().stream()
        .filter(entry -> lastEventId.compareTo((String)entry.getKey()) < 0)
        .forEach(entry -> sseEmitterService.send(
            sseEmitter, (String)entry.getKey(), entry.getValue())
        );

    redisCacheService.deleteToHash(hashKey);
  }

  @Override
  @Transactional(readOnly = true)
  public List<NotificationResponse> findNotification(Long memberId) {
    List<Notification> notifications = notificationRepository.findAllByMember_Id(memberId);
    log.info("Service : 알림 조회 완료");

    return notifications.stream().map(NotificationResponse::new)
        .collect(Collectors.toList());
  }

  // 채팅방 관련 알림 생성
  @Transactional
  public void createNotificationChatRoom(ChatRoomNotificationRequest request) {
    /** TODO
     * 특정 채팅방에서 이벤트 발생시 해당 채팅방의 인원을 조회함.
     * 실제로는 PerformanceId 와 ChatRoomId로 특정해야함.
     */
    List<ChatParticipant> chatParticipants = chatParticipantRepository.
        findAllByChatRoom_Id(request.getChatRoomId());
    log.info("Service : 참여 멤버 조회 성공");

    /** TODO
     * 회원이 탈퇴 했을 때 에러가 아닌 다른 메세지를 보내 줘야함.
     */
    Member member = memberRepository.findById(request.getTargetId())
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    log.info("Service : Target 맴버 조회 성공");
    String message = createChatRoomMessage(member.getUsername(),
        request.getMessageType()); // 메세지 생성
    log.info("Service : 알림 메세지 생성");
    String url = createChatRoomUrl(request.getChatRoomId());
    log.info("Service : url 생성");

    for (ChatParticipant chatParticipant : chatParticipants) {
      Member participantMember = chatParticipant.getMember();
      if (Objects.equals(participantMember.getId(), request.getTargetId())) {
        log.info("Service : Target은 제외");
        continue;
      }
      notificationSaveAndPublish(request, message, url, participantMember);
    }
  }

  private void notificationSaveAndPublish(ChatRoomNotificationRequest request, String message, String url,
      Member participantMember) {
    Notification notification = Notification.builder()
        .message(message)
        .url(url)
        .notificationType(NotificationType.CHATROOM)
        .createdAt(LocalDateTime.now())
        .member(participantMember)
        .build();
    notificationRepository.save(notification);
    log.info("Service : 알림 저장 성공");

    redisNotificationService.publish(
        Channel.makeChannel(request.getPerformanceId(), request.getChatRoomId()),
        new NotificationResponse(notification));
  }

  // 알림 메세지 반환
  private String createChatRoomMessage(String username, MessageType messageType) {
    if (messageType.equals(MessageType.ENTER)) {
      return username + NotificationMessage.ENTER_CHATROOM.getDescription();
    }

    if (messageType.equals(MessageType.EXIT)) {
      return username + NotificationMessage.EXIT_CHATROOM.getDescription();
    }

    if (messageType.equals(MessageType.KICK)) {
      return username + NotificationMessage.DROP_CHATROOM.getDescription();
    }
    throw new CustomException(ErrorCode.INVALID_PARAMETER);
  }

  // URL 생성
  private String createChatRoomUrl(Long chatRoomId) {
    return NotificationType.CHATROOM.getDescription() + "/" + chatRoomId + "/enter";
  }
}
