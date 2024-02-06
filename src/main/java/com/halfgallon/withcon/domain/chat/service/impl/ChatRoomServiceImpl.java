package com.halfgallon.withcon.domain.chat.service.impl;

import static com.halfgallon.withcon.domain.chat.constant.ChattingConstant.CHAT_MESSAGE_PAGE_SIZE;
import static com.halfgallon.withcon.global.exception.ErrorCode.CHATROOM_NOT_FOUND;
import static com.halfgallon.withcon.global.exception.ErrorCode.DUPLICATE_CHATROOM;
import static com.halfgallon.withcon.global.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.halfgallon.withcon.global.exception.ErrorCode.PARTICIPANT_NOT_FOUND;
import static com.halfgallon.withcon.global.exception.ErrorCode.USER_JUST_ONE_CREATE_CHATROOM;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.chat.dto.ChatMessageDto;
import com.halfgallon.withcon.domain.chat.dto.ChatMessageRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomEnterResponse;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomResponse;
import com.halfgallon.withcon.domain.chat.entity.ChatMessage;
import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import com.halfgallon.withcon.domain.chat.repository.ChatMessageRepository;
import com.halfgallon.withcon.domain.chat.repository.ChatParticipantRepository;
import com.halfgallon.withcon.domain.chat.repository.ChatRoomRepository;
import com.halfgallon.withcon.domain.chat.service.ChatRoomService;
import com.halfgallon.withcon.domain.member.dto.MemberDto;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.domain.tag.entity.Tag;
import com.halfgallon.withcon.domain.tag.repository.TagRepository;
import com.halfgallon.withcon.global.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatParticipantRepository participantRepository;
  private final MemberRepository memberRepository;
  private final TagRepository tagRepository;
  private final ChatMessageRepository chatMessageRepository;

  @Override
  public ChatRoomResponse createChatRoom(CustomUserDetails customUserDetails, ChatRoomRequest request) {
    Member member = memberRepository.findById(customUserDetails.getId())
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

    validationCreateChatroom(request, member.getId());

    ChatRoom chatRoom = chatRoomRepository.save(request.toEntity());

    //채팅방 참여 인원 저장
    chatRoom.addChatParticipant(participantRepository.save(
        ChatParticipant.builder()
            .chatRoom(chatRoom)
            .member(member)
            .isManager(true)
            .build()));

    //태그가 있는 경우에만 - 해당 태그 저장
    if (!CollectionUtils.isEmpty(request.tags())) {
      List<Tag> tagList = request.tags()
          .stream()
          .map(t -> Tag.builder()
              .name(t)
              .chatRoom(chatRoom)
              .build())
          .toList();

      tagRepository.saveAll(tagList);

      for (Tag tag : tagList) {
        chatRoom.addTag(tag);
      }
    }

    return ChatRoomResponse.fromEntity(chatRoom);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ChatRoomResponse> findChatRoom(Pageable pageable) {
    return chatRoomRepository.findAll(pageable).map(ChatRoomResponse::fromEntity);
  }

  @Override
  @Transactional
  public ChatRoomEnterResponse enterChatRoom(CustomUserDetails customUserDetails, Long chatRoomId) {
    Member member = memberRepository.findById(customUserDetails.getId())
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new CustomException(CHATROOM_NOT_FOUND));

    //채팅방 참여 인원 저장(첫 방문인 경우 데이터 저장)
    participantRepository.findByMemberIdAndChatRoomId(customUserDetails.getId(), chatRoomId)
        .ifPresentOrElse(
            participant -> {},
            () -> chatRoom.addChatParticipant(participantRepository.save(
                ChatParticipant.builder()
                    .chatRoom(chatRoom)
                    .member(member)
                    .build()))
        );

    //채팅방 참여하고 있는 인원 리스트
    List<MemberDto> members = chatRoom.getChatParticipants().stream()
        .map(p -> MemberDto.fromEntity(p.getMember())).toList();

    return ChatRoomEnterResponse.builder()
        .roomName(chatRoom.getName())
        .chatRoomId(chatRoomId)
        .userCount(chatRoom.getUserCount())
        .members(members)
        .build();
  }

  @Override
  @Transactional
  public void exitChatRoom(CustomUserDetails customUserDetails, Long chatRoomId) {
    ChatParticipant participant
        = participantRepository.findByMemberIdAndChatRoomId(customUserDetails.getId(), chatRoomId)
        .orElseThrow(() -> new CustomException(PARTICIPANT_NOT_FOUND));

    participantRepository.delete(participant);
    participant.getChatRoom().removeChatParticipant(participant);

    //채팅방 인원이 전부 나간 경우 or 채팅방 방장이 방을 없앤 경우
    if (participant.getChatRoom().getUserCount() <= 0 || participant.isManager()) {
      chatRoomRepository.delete(participant.getChatRoom());
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Slice<ChatMessageDto> findAllMessageChatRoom(CustomUserDetails customUserDetails,
      ChatMessageRequest request, Long chatRoomId) {

    participantRepository.findByMemberIdAndChatRoomId(customUserDetails.getId(), chatRoomId)
        .orElseThrow(() -> new CustomException(PARTICIPANT_NOT_FOUND));

    Slice<ChatMessage> message = chatMessageRepository.findChatRoomMessage(
        request.lastMsgId(), chatRoomId, Pageable.ofSize(CHAT_MESSAGE_PAGE_SIZE));

    return message.map(ChatMessageDto::fromEntity);
  }

  @Override
  @Transactional
  public ChatRoomResponse kickChatRoom(CustomUserDetails customUserDetails, Long chatRoomId,
      Long memberId) {
    ChatParticipant chatParticipant = participantRepository.findByMemberIdAndChatRoomId(
            memberId, chatRoomId)
        .orElseThrow(() -> new CustomException(PARTICIPANT_NOT_FOUND));

    if (participantRepository.checkRoomManager(customUserDetails.getId())) {
      participantRepository.delete(chatParticipant);
      chatParticipant.getChatRoom().removeChatParticipant(chatParticipant);
    }

    return ChatRoomResponse.fromEntity(chatParticipant.getChatRoom());
  }

  /**
   * 채팅방 생성 유효성 검사
   */
  private void validationCreateChatroom(ChatRoomRequest request, Long memberId) {
    //채팅방 이름 검사
    if (chatRoomRepository.existsByName(request.name())) {
      throw new CustomException(DUPLICATE_CHATROOM);
    }
    //채팅방은 1인당 1개만 생성이 가능하다.
    if (participantRepository.checkRoomManager(memberId)) {
      throw new CustomException(USER_JUST_ONE_CREATE_CHATROOM);
    }
  }

}
