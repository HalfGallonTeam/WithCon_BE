package com.halfgallon.withcon.domain.chat.service;

import com.halfgallon.withcon.domain.chat.dto.ChatParticipantResponse;
import java.util.List;

public interface ChatParticipantService {

  List<ChatParticipantResponse> findMyChatRoom(Long memberId);
}
