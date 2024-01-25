package com.halfgallon.withcon.domain.chat.service;

import com.halfgallon.withcon.domain.chat.constant.MessageType;
import com.halfgallon.withcon.domain.chat.dto.ChatMessageRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatMessageResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

  @Override
  public ChatMessageResponse chatMessage(ChatMessageRequest request, Long roomId) {
    return ChatMessageResponse.builder()
        .chatRoomId(roomId)
        .memberId(request.getMemberId())
        .message(request.getMessage())
        .messageType(MessageType.CHAT)
        .time(LocalDateTime.now())
        .build();
  }

  @Override
  public ChatMessageResponse enterMessage(ChatMessageRequest request, Long roomId) {

    String message = "님이 입장하였습니다.";

    return ChatMessageResponse.builder()
        .chatRoomId(roomId)
        .memberId(request.getMemberId())
        .message(message)
        .messageType(MessageType.ENTER)
        .time(LocalDateTime.now())
        .build();
  }

  @Override
  public ChatMessageResponse exitMessage(ChatMessageRequest request, Long roomId) {
    String message = "님이 퇴장하였습니다.";

    return ChatMessageResponse.builder()
        .chatRoomId(roomId)
        .memberId(request.getMemberId())
        .message(message)
        .messageType(MessageType.EXIT)
        .time(LocalDateTime.now())
        .build();
  }

}
