package com.halfgallon.withcon.domain.notification.repository;

import com.halfgallon.withcon.domain.notification.entity.Notification;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findNotificationsByMember_IdAndReadStatus(Long memberId, boolean readStatus);

  void deleteByCreatedAtBefore(LocalDateTime time);

}
