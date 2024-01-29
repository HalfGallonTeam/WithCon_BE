package com.halfgallon.withcon.domain.notification.service;


import com.halfgallon.withcon.domain.notification.dto.NotificationResponse;
import java.util.List;

public interface NotificationService {

  List<NotificationResponse> findNotification(Long memberId);
}
