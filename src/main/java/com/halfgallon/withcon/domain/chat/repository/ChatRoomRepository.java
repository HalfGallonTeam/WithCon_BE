package com.halfgallon.withcon.domain.chat.repository;

import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, CustomChatRoomRepository {
  boolean existsByNameAndPerformance_Id(String name, String performanceId);

  Page<ChatRoom> findAllByPerformance_Id(String performanceId, Pageable pageable);
}
