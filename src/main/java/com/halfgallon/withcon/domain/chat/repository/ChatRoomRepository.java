package com.halfgallon.withcon.domain.chat.repository;

import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
  boolean existsByName(String name);
}
