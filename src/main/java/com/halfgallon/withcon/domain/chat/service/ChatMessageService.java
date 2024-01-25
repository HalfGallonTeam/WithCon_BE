package com.halfgallon.withcon.domain.chat.service;

import com.halfgallon.withcon.domain.chat.dto.ChatMessageRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatMessageResponse;

public interface ChatMessageService {
  ChatMessageResponse chatMessage(ChatMessageRequest request, Long roomId);
  ChatMessageResponse enterMessage(ChatMessageRequest request, Long roomId);
  ChatMessageResponse exitMessage(ChatMessageRequest request, Long roomId);

}
