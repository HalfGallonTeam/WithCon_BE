package com.halfgallon.withcon.domain.chat.repository;

import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import com.halfgallon.withcon.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long>, CustomChatParticipantRepository {

  boolean existsByMemberId(Long MemberId);

  boolean existsByMemberIdAndChatRoomId(Long memberId, Long roomId);

  Optional<ChatParticipant> findByMemberAndChatRoom(Member member, ChatRoom chatRoom);
}
