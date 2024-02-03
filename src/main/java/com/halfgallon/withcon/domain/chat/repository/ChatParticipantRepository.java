package com.halfgallon.withcon.domain.chat.repository;

import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long>,
    CustomChatParticipantRepository {
  Optional<ChatParticipant> findByMemberIdAndChatRoomId(Long memberId, Long roomId);
}
