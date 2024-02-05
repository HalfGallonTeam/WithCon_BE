package com.halfgallon.withcon.domain.chat.service.impl;

import static com.halfgallon.withcon.global.exception.ErrorCode.PARTICIPANT_NOT_FOUND;

import com.halfgallon.withcon.domain.chat.constant.MessageType;
import com.halfgallon.withcon.domain.chat.dto.ChatMessageDto;
import com.halfgallon.withcon.domain.chat.entity.ChatMessage;
import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.chat.repository.ChatMessageRepository;
import com.halfgallon.withcon.domain.chat.repository.ChatParticipantRepository;
import com.halfgallon.withcon.domain.chat.service.ChatMessageService;
import com.halfgallon.withcon.global.exception.CustomException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

  private final ChatMessageRepository chatMessageRepository;
  private final ChatParticipantRepository participantRepository;

  @Override
  public ChatMessageDto chatMessage(ChatMessageDto request, Long roomId) {
    return ChatMessageDto.builder()
        .memberId(request.getMemberId())
        .roomId(roomId)
        .message(request.getMessage())
        .messageType(MessageType.CHAT)
        .sendAt(LocalDateTime.now())
        .build();
  }

  @Override
  public ChatMessageDto enterMessage(ChatMessageDto request, Long roomId) {
    ChatParticipant chatParticipant = participantRepository.findById(request.getMemberId())
        .orElseThrow(() -> new CustomException(PARTICIPANT_NOT_FOUND));

    String message = chatParticipant.getMember().getNickname() + "님이 입장하였습니다.";

    return ChatMessageDto.builder()
        .roomId(roomId)
        .memberId(request.getMemberId())
        .message(message)
        .messageType(MessageType.ENTER)
        .sendAt(LocalDateTime.now())
        .build();
  }

  @Override
  public ChatMessageDto exitMessage(ChatMessageDto request, Long roomId) {
    ChatParticipant chatParticipant = participantRepository.findById(request.getMemberId())
        .orElseThrow(() -> new CustomException(PARTICIPANT_NOT_FOUND));

    String message = chatParticipant.getMember().getNickname() + "님이 퇴장하였습니다.";

    return ChatMessageDto.builder()
        .roomId(roomId)
        .memberId(request.getMemberId())
        .message(message)
        .messageType(MessageType.EXIT)
        .sendAt(LocalDateTime.now())
        .build();
  }

  @Override
  public void saveChatMessage(ChatMessageDto response) {
    ChatParticipant chatParticipant = participantRepository.findByMemberIdAndChatRoomId(
            response.getMemberId(), response.getRoomId())
        .orElseThrow(() -> new CustomException(PARTICIPANT_NOT_FOUND));

    ChatMessage message = response.toEntity();
    message.updateChatParticipant(chatParticipant);
    message.updateChatRoom(chatParticipant.getChatRoom());

    chatMessageRepository.save(message);
  }

}
