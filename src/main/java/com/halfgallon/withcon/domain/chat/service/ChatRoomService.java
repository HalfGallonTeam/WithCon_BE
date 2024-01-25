package com.halfgallon.withcon.domain.chat.service;

import com.halfgallon.withcon.domain.chat.dto.ChatRoomEnterResponse;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomResponse;
import java.util.List;

public interface ChatRoomService {

  ChatRoomResponse createChatRoom(ChatRoomRequest request);

  List<ChatRoomResponse> findChatRoom();

  ChatRoomEnterResponse enterChatRoom(Long chatRoomId, Long memberId);

  void exitChatRoom(Long chatRoomId, Long memberId);
}
