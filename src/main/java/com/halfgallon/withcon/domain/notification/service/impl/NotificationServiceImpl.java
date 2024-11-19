package com.halfgallon.withcon.domain.notification.service.impl;

import static com.halfgallon.withcon.domain.chat.constant.MessageType.ENTER;
import static com.halfgallon.withcon.domain.chat.constant.MessageType.EXIT;
import static com.halfgallon.withcon.domain.chat.constant.MessageType.KICK;
import static com.halfgallon.withcon.domain.notification.constant.NotificationMessage.ENTER_CHATROOM;
import static com.halfgallon.withcon.domain.notification.constant.NotificationMessage.EXIT_CHATROOM;
import static com.halfgallon.withcon.domain.notification.constant.NotificationMessage.KICK_CHATROOM;
import static com.halfgallon.withcon.domain.notification.constant.NotificationMessage.SUBSCRIBE;
import static com.halfgallon.withcon.global.exception.ErrorCode.INVALID_PARAMETER;
import static com.halfgallon.withcon.global.exception.ErrorCode.NOT_EXIST_NOTIFICATION;

import com.halfgallon.withcon.domain.chat.constant.MessageType;
import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.chat.repository.ChatParticipantRepository;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.domain.notification.constant.Channel;
import com.halfgallon.withcon.domain.notification.constant.NotificationType;
import com.halfgallon.withcon.domain.notification.constant.RedisCacheType;
import com.halfgallon.withcon.domain.notification.constant.VisibleType;
import com.halfgallon.withcon.domain.notification.dto.ChatRoomNotificationRequest;
import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.entity.Notification;
import com.halfgallon.withcon.domain.notification.repository.NotificationRepository;
import com.halfgallon.withcon.domain.notification.repository.SseEmitterRepository;
import com.halfgallon.withcon.domain.notification.service.NotificationService;
import com.halfgallon.withcon.domain.notification.service.RedisNotificationService;
import com.halfgallon.withcon.domain.notification.service.RedisService;
import com.halfgallon.withcon.domain.notification.service.SseEmitterService;
import com.halfgallon.withcon.global.exception.CustomException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
  private final RedisService redisService;

  @Override
  public SseEmitter subscribe(Long memberId) {
    String emitterId = createEmitterId(memberId);
    SseEmitter sseEmitter = sseEmitterRepository.save(emitterId, new SseEmitter(TIME_OUT));

    // 더미 데이터(503 에러 방지)
    sseEmitterService.send(sseEmitter, emitterId,
        SUBSCRIBE.getDescription() + memberId + "\"}");
    log.info("SSE 구독 완료");

    redisNotificationService.subscribe(String.valueOf(memberId));

    sseEmitter.onCompletion(() -> {
      log.info("onCompletion callback");
      sseEmitterRepository.deleteById(emitterId);
      redisNotificationService.unsubscribe(String.valueOf(memberId));
    });
    sseEmitter.onError((e) -> {
      log.info("onError callback");
      sseEmitter.complete();
    });
    sseEmitter.onTimeout(() -> {
      log.info("onTimeout callback");
      sseEmitterRepository.deleteById(emitterId);
      redisNotificationService.unsubscribe(String.valueOf(memberId));
    });

    return sseEmitter;
  }

  private String createEmitterId(Long memberId) {
    return memberId + "_" + System.currentTimeMillis();
  }

  @Override
  @Transactional(readOnly = true)
  public List<NotificationResponse> findNotification(Long memberId) {
    List<Notification> notifications =
        notificationRepository.findNotificationsByMember_IdAndReadStatus(memberId, false);
    log.info("Service : 알림 조회 완료");

    return notifications.stream().map(NotificationResponse::new)
        .collect(Collectors.toList());
  }

  // 채팅방 관련 알림 생성
  @Transactional
  public void createNotificationChatRoom(ChatRoomNotificationRequest request) {
    List<ChatParticipant> chatParticipants = chatParticipantRepository.
        findAllByChatRoom_Id(request.getChatRoomId());
    log.info("참여 맴버 조회 : {}", chatParticipants);

    String message = createMessageOfTarget(request);
    String url = createChatRoomUrl(request.getChatRoomId());

    String visibleKey = RedisCacheType.VISIBLE_CACHE.getDescription()
        + request.getChatRoomId();
    log.info("채널명 : {}", visibleKey);

    Map<Object, Object> cache = redisService.getHashByKey(visibleKey);
    log.info("Visible 캐시 데이터 조회 : {} ", cache);
    if(cache == null) {
      return;
    }

    for (ChatParticipant chatParticipant : chatParticipants) {
      Member participantMember = chatParticipant.getMember();

      if (Objects.equals(participantMember.getId(), request.getTargetId())) {
        log.info("Service : Target은 제외");
        continue;
      }

      if(!cache.containsKey(String.valueOf(participantMember.getId()))) {
        continue;
      }

      VisibleType visibleType = VisibleType.valueOf(
          (String)cache.get(String.valueOf(participantMember.getId())));

      if(visibleType == VisibleType.HIDDEN || visibleType == VisibleType.NONE) {
        notificationSaveAndPublish(request, message, url, participantMember);
      }
    }
  }

  private String createMessageOfTarget(ChatRoomNotificationRequest request) {
    Member member = memberRepository.findById(request.getTargetId())
        .orElse(withdrawMember());
    String message = createChatRoomMessage(member.getUsername(),
        request.getMessageType()); // 메세지 생성
    log.info("알림 메시지 생성 : {}", message);
    return message;
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

    redisNotificationService.publish(Channel.CHATROOM_CHANNEL + request.getChatRoomId(),
        new NotificationResponse(notification));
  }

  // 알림 메세지 반환
  private String createChatRoomMessage(String username, MessageType messageType) {
    StringBuilder sb = new StringBuilder(username);
    if (messageType.equals(ENTER)) {
      sb.append(ENTER_CHATROOM.getDescription());
    } else if (messageType.equals(EXIT)) {
      sb.append(EXIT_CHATROOM.getDescription());
    } else if (messageType.equals(KICK)) {
      sb.append(KICK_CHATROOM.getDescription());
    } else {
      throw new CustomException(INVALID_PARAMETER);
    }
    return sb.toString();
  }

  // URL 생성
  private String createChatRoomUrl(Long chatRoomId) {
    String url = NotificationType.CHATROOM.getDescription() + "/" + chatRoomId + "/enter";
    log.info("url 생성 : {}",url);
    return url;
  }

  private Member withdrawMember() {
    return Member.builder()
        .username("탈퇴한 회원")
        .build();
  }

  @Override
  public void readNotification(Long notificationId) {
    Notification notification =
        notificationRepository.findById(notificationId)
            .orElseThrow(() -> new CustomException(NOT_EXIST_NOTIFICATION));

    notification.updateReadStatus();
    notificationRepository.save(notification);
    log.info("Service : 알림 읽음");
  }
}
