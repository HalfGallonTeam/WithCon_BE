package com.halfgallon.withcon.domain.chat.service;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomEnterResponse;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomService {

  ChatRoomResponse createChatRoom(CustomUserDetails customUserDetails, ChatRoomRequest request);

  Page<ChatRoomResponse> findChatRoom(Pageable pageable);

  ChatRoomEnterResponse enterChatRoom(CustomUserDetails customUserDetails, Long chatRoomId);

  void exitChatRoom(CustomUserDetails customUserDetails, Long chatRoomId);
}
