package com.halfgallon.withcon.domain.chat.repository;

import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import com.halfgallon.withcon.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

  boolean existsByMemberId(Long MemberId);

  List<ChatParticipant> findAllByMemberId(Long id);

  boolean existsByMemberIdAndChatRoomId(Long memberId, Long roomId);

  Optional<ChatParticipant> findByMemberAndChatRoom(Member member, ChatRoom chatRoom);

  @Query("SELECT COUNT(c.id) > 0 FROM ChatParticipant c "
      + " WHERE c.member.id = :memberId AND c.isManager = true ")
  boolean existRoomLeader(@Param("memberId") Long memberId);

}
