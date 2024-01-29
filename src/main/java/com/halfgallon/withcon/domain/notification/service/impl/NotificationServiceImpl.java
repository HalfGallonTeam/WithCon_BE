package com.halfgallon.withcon.domain.notification.service.impl;

import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import com.halfgallon.withcon.domain.notification.entity.Notification;
import com.halfgallon.withcon.domain.notification.repository.NotificationRepository;
import com.halfgallon.withcon.domain.notification.service.NotificationService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;

  @Override
  public List<NotificationResponse> findNotification(Long memberId) {
    List<Notification> notifications =  notificationRepository.findAllByMember_Id(memberId);

    return notifications.stream().map(NotificationResponse::new)
        .collect(Collectors.toList());

  }
}
