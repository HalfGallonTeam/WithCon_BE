package com.halfgallon.withcon.domain.chat.service.impl;

import static com.halfgallon.withcon.global.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.halfgallon.withcon.domain.chat.constant.MessageType;
import com.halfgallon.withcon.domain.chat.dto.ChatMessageDto;
import com.halfgallon.withcon.domain.chat.entity.ChatMessage;
import com.halfgallon.withcon.domain.chat.repository.ChatMessageRepository;
import com.halfgallon.withcon.domain.chat.repository.ChatParticipantRepository;
import com.halfgallon.withcon.domain.chat.service.ChatMessageService;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

  private final MemberRepository memberRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final ChatParticipantRepository participantRepository;

  @Override
  public ChatMessageDto chatMessage(ChatMessageDto request, Long roomId) {
    Member member = findMemberOrThrow(request);

    return ChatMessageDto.builder()
        .memberId(request.getMemberId())
        .roomId(roomId)
        .message(request.getMessage())
        .messageType(MessageType.CHAT)
        .sendAt(System.currentTimeMillis())
        .nickName(member.getNickname())
        .userProfile(member.getProfileImage())
        .build();
  }

  @Override
  public ChatMessageDto enterMessage(ChatMessageDto request, Long roomId) {
    Member member = findMemberOrThrow(request);
    String message = member.getNickname() + "님이 입장하였습니다.";

    return ChatMessageDto.builder()
        .roomId(roomId)
        .memberId(request.getMemberId())
        .message(message)
        .messageType(MessageType.ENTER)
        .sendAt(System.currentTimeMillis())
        .nickName(member.getNickname())
        .userProfile(member.getProfileImage())
        .build();
  }

  @Override
  public ChatMessageDto exitMessage(ChatMessageDto request, Long roomId) {
    Member member = findMemberOrThrow(request);
    String message = member.getNickname() + "님이 퇴장하였습니다.";

    return ChatMessageDto.builder()
        .roomId(roomId)
        .memberId(request.getMemberId())
        .message(message)
        .messageType(MessageType.EXIT)
        .sendAt(System.currentTimeMillis())
        .nickName(member.getNickname())
        .userProfile(member.getProfileImage())
        .build();
  }

  private Member findMemberOrThrow(ChatMessageDto request) {
    return memberRepository.findById(request.getMemberId())
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
  }

  @Override
  public ChatMessageDto kickMessage(ChatMessageDto request, Long roomId) {
    Member member = findMemberOrThrow(request);
    String message = member.getNickname() + "님이 강퇴당했습니다.";

    return ChatMessageDto.builder()
        .roomId(roomId)
        .memberId(request.getMemberId())
        .message(message)
        .messageType(MessageType.KICK)
        .sendAt(System.currentTimeMillis())
        .nickName(member.getNickname())
        .userProfile(member.getProfileImage())
        .build();
  }

  @Override
  public void saveChatMessage(ChatMessageDto response) {
    participantRepository.findByMemberIdAndChatRoomId(response.getMemberId(), response.getRoomId())
        .ifPresent(e -> {
          ChatMessage message = response.toEntity();
          message.updateChatParticipant(e);
          message.updateChatRoom(e.getChatRoom());

          chatMessageRepository.save(message);
          log.info("saveChatMessage -> message.getMessage() : {}", message.getMessage());
        });
  }

}
