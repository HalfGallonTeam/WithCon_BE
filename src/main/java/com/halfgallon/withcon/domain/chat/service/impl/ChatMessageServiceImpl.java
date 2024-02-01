package com.halfgallon.withcon.domain.chat.service.impl;

import static com.halfgallon.withcon.global.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.halfgallon.withcon.domain.chat.constant.MessageType;
import com.halfgallon.withcon.domain.chat.dto.ChatMessageDto;
import com.halfgallon.withcon.domain.chat.service.ChatMessageService;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.global.exception.CustomException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

  private final MemberRepository memberRepository;

  @Override
  public ChatMessageDto chatMessage(ChatMessageDto request, Long roomId) {
    return ChatMessageDto.builder()
        .memberId(request.getMemberId())
        .roomId(roomId)
        .message(request.getMessage())
        .messageType(MessageType.CHAT)
        .time(LocalDateTime.now())
        .build();
  }

  @Override
  public ChatMessageDto enterMessage(ChatMessageDto request, Long roomId) {

    Member member = memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

    String message = member.getNickname() + "님이 입장하였습니다.";

    return ChatMessageDto.builder()
        .roomId(roomId)
        .memberId(request.getMemberId())
        .message(message)
        .messageType(MessageType.ENTER)
        .time(LocalDateTime.now())
        .build();
  }

  @Override
  public ChatMessageDto exitMessage(ChatMessageDto request, Long roomId) {

    Member member = memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

    String message = member.getNickname() + "님이 퇴장하였습니다.";

    return ChatMessageDto.builder()
        .roomId(roomId)
        .memberId(request.getMemberId())
        .message(message)
        .messageType(MessageType.EXIT)
        .time(LocalDateTime.now())
        .build();
  }

}
