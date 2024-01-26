package com.halfgallon.withcon.domain.chat.repository;

import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import com.halfgallon.withcon.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

  boolean existsByMemberId(Long MemberId);

  @Query("SELECT cp FROM ChatParticipant cp "
      + " JOIN FETCH cp.chatRoom cr "
      + " WHERE cp.member.id = :memberId"
      + " ORDER BY cr.createdAt DESC ")
  Page<ChatParticipant> findAllByMemberId(@Param("memberId") Long memberId, Pageable pageable);

  boolean existsByMemberIdAndChatRoomId(Long memberId, Long roomId);

  Optional<ChatParticipant> findByMemberAndChatRoom(Member member, ChatRoom chatRoom);

  @Query("SELECT COUNT(c.id) > 0 FROM ChatParticipant c "
      + " WHERE c.member.id = :memberId AND c.isManager = true ")
  boolean existRoomLeader(@Param("memberId") Long memberId);

}
