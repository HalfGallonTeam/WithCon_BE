package com.halfgallon.withcon.domain.notification.service.handler;

import com.halfgallon.withcon.domain.notification.constant.NotificationMessage;
import com.halfgallon.withcon.domain.notification.constant.NotificationType;
import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.entity.Notification;
import com.halfgallon.withcon.domain.notification.repository.NotificationRepository;
import com.halfgallon.withcon.domain.notification.service.RedisNotificationService;
import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import com.halfgallon.withcon.domain.performance.entitiy.PerformanceLike;
import com.halfgallon.withcon.domain.performance.repository.PerformanceLikeRepository;
import com.halfgallon.withcon.domain.performance.repository.PerformanceRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduleHandler {

  private final RedisNotificationService redisNotificationService;
  private final NotificationRepository notificationRepository;
  private final PerformanceRepository performanceRepository;
  private final PerformanceLikeRepository performanceLikeRepository;

  @Transactional
  @Scheduled(cron = "0 0 1 * * *") // 매일 새벽 1시에 알림 데이터 삭제
  public void clearNotification() {
    LocalDateTime agoTime = LocalDateTime.now().minusDays(3); // 3일전
    notificationRepository.deleteByCreatedAtBefore(agoTime);
    log.info("과거 알림 데이터 삭제 실행");
  }

  @Transactional
  @Scheduled(cron = "0 */5 20-22 * * *") // 매일 자정에 오픈 공연 조회 후 알림 생성
  public void createPerformanceOpenNotification() {
    LocalDate today = LocalDate.now();
    LocalDate day = today.minusDays(21);
    List<Performance> todayPerformances = performanceRepository.findAllByStartDate(day);
    log.info("Scheduler : 오픈 당일 공연 조회 성공");

    todayPerformances.forEach(performance -> {
      List<PerformanceLike> performanceLikes =
          performanceLikeRepository.findPerformanceLikeByPerformance_Id(performance.getId());

      performanceLikes.forEach(performanceLike ->
        performanceNotificationAndPublish(performance, performanceLike));
    });
  }

  private void performanceNotificationAndPublish(Performance performance, PerformanceLike performanceLike) {
    Notification notification = Notification.builder()
        .message(createPerformanceNotification(performance))
        .url(createPerformanceUrl(performance))
        .notificationType(NotificationType.PERFORMANCE)
        .createdAt(LocalDateTime.now())
        .member(performanceLike.getMember())
        .build();

    notificationRepository.save(notification);
    log.info("공연 : 알림 저장 성공 ");

    redisNotificationService.publish(String.valueOf(performanceLike.getMember().getId()),
        new NotificationResponse(notification));
    log.info("공연 : 메세지 발행 ");
  }

  private String createPerformanceUrl(Performance performance) {
    return NotificationType.PERFORMANCE.getDescription() + "/" + performance.getId();
  }

  private String createPerformanceNotification(Performance performance) {
    return performance.getName() + NotificationMessage.OPEN_PERFORMANCE.getDescription();
  }
}

