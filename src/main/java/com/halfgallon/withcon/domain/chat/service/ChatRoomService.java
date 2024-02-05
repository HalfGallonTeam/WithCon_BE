package com.halfgallon.withcon.domain.chat.service;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.chat.dto.ChatMessageDto;
import com.halfgallon.withcon.domain.chat.dto.ChatMessageRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomEnterResponse;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatRoomService {

  ChatRoomResponse createChatRoom(CustomUserDetails customUserDetails, ChatRoomRequest request);

  Page<ChatRoomResponse> findChatRoom(Pageable pageable);

  ChatRoomEnterResponse enterChatRoom(CustomUserDetails customUserDetails, Long chatRoomId);

  void exitChatRoom(CustomUserDetails customUserDetails, Long chatRoomId);

  Slice<ChatMessageDto> findAllMessageChatRoom(CustomUserDetails customUserDetails, ChatMessageRequest request,
      Long chatRoomId);
}
