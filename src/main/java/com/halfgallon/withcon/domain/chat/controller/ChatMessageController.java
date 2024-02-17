package com.halfgallon.withcon.domain.chat.controller;

import static com.halfgallon.withcon.domain.chat.constant.ChattingConstant.CHAT_EXCHANGE_NAME;

import com.halfgallon.withcon.domain.chat.constant.ChattingConstant;
import com.halfgallon.withcon.domain.chat.dto.ChatMessageDto;
import com.halfgallon.withcon.domain.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

  private final RabbitTemplate rabbitTemplate;
  private final ChatMessageService chatMessageService;

  @MessageMapping("/chat/message/{roomId}")
  public void sendMessage(@Payload ChatMessageDto chatDto, @DestinationVariable("roomId") Long roomId) {
    ChatMessageDto chatMessageDto = chatMessageService.chatMessage(chatDto, roomId);
    rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + roomId, chatMessageDto);
  }

  @MessageMapping("/chat/enter/{roomId}")
  public void enterMember(@Payload ChatMessageDto chatDto, @DestinationVariable("roomId") Long roomId) {
    rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + roomId,
        chatMessageService.enterMessage(chatDto, roomId));
  }

  @MessageMapping("/chat/exit/{roomId}")
  public void exitMember(@Payload ChatMessageDto chatDto, @DestinationVariable("roomId") Long roomId) {
    rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + roomId,
        chatMessageService.exitMessage(chatDto, roomId));
  }

  @MessageMapping("/chat/kick/{roomId}")
  public void kickMember(@Payload ChatMessageDto chatDto, @DestinationVariable("roomId") Long roomId) {
    rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + roomId,
        chatMessageService.kickMessage(chatDto, roomId));
  }

  //기본적으로 chat.queue가 exchange에 바인딩 되어있기 때문에 모든 메시지 처리
  @RabbitListener(queues = ChattingConstant.CHAT_QUEUE_NAME)
  public void receive(ChatMessageDto response) {
    chatMessageService.saveChatMessage(response);
  }

}
