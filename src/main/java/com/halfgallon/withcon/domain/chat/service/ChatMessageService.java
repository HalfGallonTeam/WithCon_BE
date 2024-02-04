package com.halfgallon.withcon.domain.chat.service;

import com.halfgallon.withcon.domain.chat.dto.ChatMessageDto;

public interface ChatMessageService {
  ChatMessageDto chatMessage(ChatMessageDto request, Long roomId);
  ChatMessageDto enterMessage(ChatMessageDto request, Long roomId);
  ChatMessageDto exitMessage(ChatMessageDto request, Long roomId);
  void saveChatMessage(ChatMessageDto response);
}
