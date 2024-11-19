package com.halfgallon.withcon.domain.chat.service;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.chat.dto.ChatLastMessageRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatMessageResponse;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomEnterResponse;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatRoomService {

  ChatRoomResponse createChatRoom(CustomUserDetails customUserDetails, ChatRoomRequest request);

  Page<ChatRoomResponse> findChatRoom(String performanceId, Pageable pageable);

  ChatRoomEnterResponse enterChatRoom(CustomUserDetails customUserDetails, Long chatRoomId);

  void exitChatRoom(CustomUserDetails customUserDetails, Long chatRoomId);

  ChatRoomResponse kickChatRoom(CustomUserDetails customUserDetails, Long chatRoomId, Long memberId);

  Slice<ChatMessageResponse> findAllMessageChatRoom(CustomUserDetails customUserDetails,
      ChatLastMessageRequest request, Long chatRoomId);

  Page<ChatRoomResponse> findAllTagNameChatRoom(String performanceId, String tagName, Pageable pageable);
}
