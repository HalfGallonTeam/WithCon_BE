package com.halfgallon.withcon.domain.notification.repository;

import com.halfgallon.withcon.domain.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findAllByMember_Id(Long memberId);
}
