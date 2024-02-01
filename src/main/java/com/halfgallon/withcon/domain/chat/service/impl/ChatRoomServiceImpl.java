package com.halfgallon.withcon.domain.chat.service.impl;

import static com.halfgallon.withcon.global.exception.ErrorCode.ALREADY_PARTICIPANT_CHATTING;
import static com.halfgallon.withcon.global.exception.ErrorCode.CHATROOM_NOT_FOUND;
import static com.halfgallon.withcon.global.exception.ErrorCode.DUPLICATE_CHATROOM;
import static com.halfgallon.withcon.global.exception.ErrorCode.PARTICIPANT_NOT_FOUND;
import static com.halfgallon.withcon.global.exception.ErrorCode.USER_JUST_ONE_CREATE_CHATROOM;
import static com.halfgallon.withcon.global.exception.ErrorCode.USER_NOT_FOUND;

import com.halfgallon.withcon.domain.chat.dto.ChatRoomEnterResponse;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomResponse;
import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatParticipantRepository participantRepository;
  private final MemberRepository memberRepository;
  private final TagRepository tagRepository;

  @Override
  public ChatRoomResponse createChatRoom(ChatRoomRequest request) {
    //멤버 조회 검사(@AuthenticationPrincipal) -> 임시로 memberRepository에서 들고와서 진행
    Member member = memberRepository.findById(request.memberId())
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    validationCreateChatroom(request);

    ChatRoom chatRoom = chatRoomRepository.save(request.toEntity());

    //채팅방 참여 인원 저장
    chatRoom.addChatParticipant(participantRepository.save(
        ChatParticipant.builder()
            .chatRoom(chatRoom)
            .member(member)
            .isManager(true)
            .build()));

    //태그가 있는 경우에만 - 해당 태그 저장
    if (request.tags() != null) {
      List<Tag> tagList = request.tags()
          .stream()
          .map(t -> Tag.builder()
              .name(t)
              .chatRoom(chatRoom)
              .build())
          .toList();

      tagRepository.saveAll(tagList);
      tagList.forEach(chatRoom::addTag);
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
  public ChatRoomEnterResponse enterChatRoom(Long chatRoomId, Long memberId) {
    //멤버 조회 검사(@AuthenticationPrincipal) -> 임시 멤버 생성
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new CustomException(CHATROOM_NOT_FOUND));

    //동일 인물이 채팅방에 중복으로 들어오는 것을 방지 추가
    if (participantRepository.existsByMemberIdAndChatRoomId(memberId, chatRoomId)) {
      throw new CustomException(ALREADY_PARTICIPANT_CHATTING);
    }

    //채팅방 참여 인원 저장
    chatRoom.addChatParticipant(participantRepository.save(
        ChatParticipant.builder()
            .chatRoom(chatRoom)
            .member(member)
            .build()));

    //채팅방 참여하고 있는 인원 리스트
    List<MemberDto> members = chatRoom.getChatParticipants().stream()
        .map(p -> MemberDto.fromEntity(p.getMember())).toList();

    return ChatRoomEnterResponse.builder()
        .chatRoomName(chatRoom.getName())
        .chatRoomId(chatRoomId)
        .userCount(chatRoom.getUserCount())
        .members(members)
        .build();
  }

  @Override
  @Transactional
  public void exitChatRoom(Long chatRoomId, Long memberId) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new CustomException(CHATROOM_NOT_FOUND));

    ChatParticipant participant = participantRepository.findByMemberAndChatRoom(member, chatRoom)
        .orElseThrow(() -> new CustomException(PARTICIPANT_NOT_FOUND));

    participantRepository.delete(participant);
    chatRoom.removeChatParticipant(participant);

    //채팅방 인원이 전부 나간 경우 or 채팅방 방장이 방을 없앤 경우
    if (chatRoom.getUserCount() <= 0 || participant.isManager()) {
      chatRoomRepository.delete(chatRoom);
    }
  }

  /**
   * 채팅방 생성 유효성 검사
   */
  private void validationCreateChatroom(ChatRoomRequest request) {
    //채팅방 이름 검사
    if (chatRoomRepository.existsByName(request.name())) {
      throw new CustomException(DUPLICATE_CHATROOM);
    }
    //채팅방은 1인당 1개만 생성이 가능하다.
    if (participantRepository.checkRoomManager(request.memberId())) {
      throw new CustomException(USER_JUST_ONE_CREATE_CHATROOM);
    }
  }

}
