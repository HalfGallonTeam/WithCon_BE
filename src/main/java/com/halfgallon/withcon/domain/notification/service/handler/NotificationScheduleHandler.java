package com.halfgallon.withcon.domain.notification.service.handler;

import com.halfgallon.withcon.domain.notification.repository.NotificationRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduleHandler {

  private final NotificationRepository notificationRepository;

  @Transactional
  @Scheduled(cron = "0 0 0 * * *" ) // 매일 자정
  public void clearNotification() {
    LocalDateTime agoTime = LocalDateTime.now().minusDays(3); // 3일전
    notificationRepository.deleteByCreatedAtBefore(agoTime);
    log.info("과거 알림 데이터 삭제 실행");
  }
}
