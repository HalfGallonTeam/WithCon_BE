package com.halfgallon.withcon.domain.chat.service;

import com.halfgallon.withcon.domain.chat.dto.ChatParticipantResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatParticipantService {

  Page<ChatParticipantResponse> findMyChatRoom(Long memberId, Pageable pageable);
}
