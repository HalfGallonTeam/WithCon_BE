package com.halfgallon.withcon.domain.chat.service;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.chat.dto.ChatLastReadMessage;
import com.halfgallon.withcon.domain.chat.dto.ChatParticipantResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatParticipantService {

  Page<ChatParticipantResponse> findMyChatRoom(CustomUserDetails customUserDetails, Pageable pageable);

  ChatLastReadMessage.Response readToLastChat(ChatLastReadMessage.Request request);
}
