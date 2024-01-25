package com.halfgallon.withcon.domain.chat.controller;

import com.halfgallon.withcon.domain.chat.dto.ChatMessageRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatMessageResponse;
import com.halfgallon.withcon.domain.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {

  private final SimpMessageSendingOperations sendingOperations;
  private final ChatMessageService chatMessageService;

  @MessageMapping("/chat/message/{roomId}")
  public void sendMessage(@Payload ChatMessageRequest chat,
                          @DestinationVariable("roomId") Long roomId) {

    ChatMessageResponse response = chatMessageService.chatMessage(chat, roomId);

    sendingOperations.convertAndSend("/topic/chatRoom/" + roomId, response.getMessage());
    log.info("Message [{}] send by member: {} to chatting room: {}",
        chat.getMessage(), chat.getMemberId(), roomId);
  }

  @MessageMapping("/chat/enter/{roomId}")
  public void enterMember(@Payload ChatMessageRequest chat,
                          @DestinationVariable("roomId") Long roomId) {

    ChatMessageResponse response = chatMessageService.enterMessage(chat, roomId);

    sendingOperations.convertAndSend("/topic/chatRoom/" + roomId, response.getMessage());
    log.info("Message [{}] send by member: {} to chatting room: {}",
        response.getMessage(), chat.getMemberId(), roomId);
  }

  @MessageMapping("/chat/exit/{roomId}")
  public void exitMember(@Payload ChatMessageRequest chat,
                          @DestinationVariable("roomId") Long roomId) {

    ChatMessageResponse response = chatMessageService.exitMessage(chat, roomId);

    sendingOperations.convertAndSend("/topic/chatRoom/" + roomId, response.getMessage());
    log.info("Message [{}] send by member: {} to chatting room: {}",
        response.getMessage(), chat.getMemberId(), roomId);
  }

}
