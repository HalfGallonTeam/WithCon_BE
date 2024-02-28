package com.halfgallon.withcon.domain.chat.repository;

import com.halfgallon.withcon.domain.chat.entity.ChatMessage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>,
    CustomChatMessageRepository{
  Optional<ChatMessage> findTopByChatRoomIdOrderBySendAtDesc(Long chatRoomId);

  List<ChatMessage> findAllByChatRoomIdAndChatParticipantId(Long chatRoomId, Long ChatParticipantId);
}
