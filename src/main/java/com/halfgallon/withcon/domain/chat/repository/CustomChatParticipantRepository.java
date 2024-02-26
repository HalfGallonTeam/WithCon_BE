package com.halfgallon.withcon.domain.chat.repository;

import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomChatParticipantRepository {

  boolean checkRoomManagerId(Long managerId);

  Page<ChatParticipant> findAllMyChattingRoom(Long memberId, Pageable pageable);
}
