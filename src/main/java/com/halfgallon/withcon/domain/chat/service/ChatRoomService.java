package com.halfgallon.withcon.domain.chat.service;

import com.halfgallon.withcon.domain.chat.dto.ChatRoomEnterResponse;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomService {

  ChatRoomResponse createChatRoom(ChatRoomRequest request);

  Page<ChatRoomResponse> findChatRoom(Pageable pageable);

  ChatRoomEnterResponse enterChatRoom(Long chatRoomId, Long memberId);

  void exitChatRoom(Long chatRoomId, Long memberId);
}
