package com.halfgallon.withcon.domain.chat.repository;

import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomChatRoomRepository {

  Page<ChatRoom> findAllTagNameChatRoom(String performanceId, String tagName, Pageable pageable);

}
