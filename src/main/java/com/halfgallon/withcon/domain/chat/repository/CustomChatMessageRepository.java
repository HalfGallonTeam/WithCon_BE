package com.halfgallon.withcon.domain.chat.repository;

import com.halfgallon.withcon.domain.chat.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomChatMessageRepository {
  Slice<ChatMessage> findChatRoomMessage(Long messageId, Long roomId, Pageable pageable);
}
