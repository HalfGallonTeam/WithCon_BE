package com.halfgallon.withcon.domain.notification.repository;

import com.halfgallon.withcon.domain.notification.entity.NotificationCache;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationCacheRepository extends
    CrudRepository<NotificationCache, Long> {

    Optional<NotificationCache> findByMemberId(Long memberId);
}
