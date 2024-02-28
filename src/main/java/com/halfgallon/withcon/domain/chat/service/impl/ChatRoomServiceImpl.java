package com.halfgallon.withcon.domain.chat.service.impl;

import static com.halfgallon.withcon.domain.chat.constant.ChattingConstant.CHAT_MESSAGE_PAGE_SIZE;
import static com.halfgallon.withcon.domain.chat.constant.EnterStatus.ALREADY;
import static com.halfgallon.withcon.domain.chat.constant.EnterStatus.NEW;
import static com.halfgallon.withcon.global.exception.ErrorCode.CHATROOM_NOT_FOUND;
import static com.halfgallon.withcon.global.exception.ErrorCode.DUPLICATE_CHATROOM_NAME;
import static com.halfgallon.withcon.global.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.halfgallon.withcon.global.exception.ErrorCode.PARTICIPANT_NOT_FOUND;
import static com.halfgallon.withcon.global.exception.ErrorCode.PERFORMANCE_NOT_FOUND;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.chat.dto.ChatLastMessageRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatMessageDto;
import com.halfgallon.withcon.domain.chat.dto.ChatParticipantDto;
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
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.domain.performance.dto.response.PerformanceResponse;
import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import com.halfgallon.withcon.domain.performance.repository.PerformanceRepository;
import com.halfgallon.withcon.domain.tag.entity.Tag;
import com.halfgallon.withcon.domain.tag.entity.TagSearch;
import com.halfgallon.withcon.domain.tag.repository.TagRepository;
import com.halfgallon.withcon.domain.tag.repository.TagSearchRepository;
import com.halfgallon.withcon.global.exception.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatParticipantRepository participantRepository;
  private final MemberRepository memberRepository;
  private final TagRepository tagRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final TagSearchRepository tagSearchRepository;
  private final PerformanceRepository performanceRepository;

  @Override
  public ChatRoomResponse createChatRoom(CustomUserDetails customUserDetails, ChatRoomRequest request) {
    Member member = memberRepository.findById(customUserDetails.getId())
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

    //채팅방 생성 시에 공연 정보 추가
    Performance performance = performanceRepository.findById(request.performanceId())
        .orElseThrow(() -> new CustomException(PERFORMANCE_NOT_FOUND));

    //동일한 공연에 동일 채팅방 이름은 사용 불가
    if (chatRoomRepository.existsByNameAndPerformance_Id(request.roomName(), performance.getId())) {
      throw new CustomException(DUPLICATE_CHATROOM_NAME);
    }

    //채팅방 데이터 저장
    ChatRoom chatRoom = chatRoomRepository.save(getChatRoom(request, member, performance));

    log.info("공연 정보 설정 완료");

    //채팅방 참여 인원 저장
    chatRoom.addChatParticipant(participantRepository.save(
        ChatParticipant.builder()
            .chatRoom(chatRoom)
            .member(member)
            .build()));

    log.info("채팅방 참여자 정보 설정 완료");

    //태그가 있는 경우에만 - 해당 태그 저장
    if (!CollectionUtils.isEmpty(request.tags())) {
      List<Tag> tagList = request.tags()
          .stream()
          .map(t -> Tag.builder()
              .name(t)
              .chatRoom(chatRoom)
              .performance(performance)
              .build())
          .toList();

      tagRepository.saveAll(tagList);

      for (Tag tag : tagList) {
        chatRoom.addTag(tag);
      }

      //태그 기록(ElasticSearch 저장)
      upsertTagSearch(tagList);
    }

    log.info("채팅방 태그 정보 설정 완료");

    return ChatRoomResponse.fromEntity(chatRoom);
  }

  private static ChatRoom getChatRoom(ChatRoomRequest request,
      Member member, Performance performance) {
    return ChatRoom.builder()
        .name(request.roomName())
        .managerId(member.getId())
        .performance(performance)
        .build();
  }

  private void upsertTagSearch(List<Tag> tagList) {
    List<TagSearch> searches = tagList.stream()
        .map(tag -> {
          TagSearch tagSearch = tagSearchRepository.findByNameAndPerformanceId(tag.getName(),
              tag.getPerformance().getId()).orElse(null);

          if (tagSearch == null) {
            return TagSearch.builder()
                .id(tag.getId().toString())
                .name(tag.getName())
                .performanceId(tag.getPerformance().getId())
                .tagCount(1)
                .build();
          } else {
            Integer count = tagRepository.countTagByNameAndPerformance_Id(tag.getName(),
                tag.getPerformance().getId());
            tagSearchRepository.updateSearchTag(tagSearch, count);

            return TagSearch.builder()
                .id(tagSearch.getId())
                .name(tagSearch.getName())
                .performanceId(tagSearch.getPerformanceId())
                .tagCount(count)
                .build();
          }
        }).toList();

    tagSearchRepository.saveAll(searches);
  }


  @Override
  @Transactional(readOnly = true)
  public Page<ChatRoomResponse> findChatRoom(String performanceId, Pageable pageable) {
    return chatRoomRepository.findAllByPerformance_Id(performanceId, pageable)
        .map(ChatRoomResponse::fromEntity);
  }

  @Override
  @Transactional
  public ChatRoomEnterResponse enterChatRoom(CustomUserDetails customUserDetails, Long chatRoomId) {
    boolean isEnter = false; //입장 여부 체크 : 첫 입장: true, 재입장: false

    Member member = memberRepository.findById(customUserDetails.getId())
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new CustomException(CHATROOM_NOT_FOUND));

    //채팅방 참여 인원 저장(첫 방문인 경우 데이터 저장)
    if (!participantRepository.existsByMemberIdAndChatRoomId(customUserDetails.getId(), chatRoomId)) {
      chatRoom.addChatParticipant(participantRepository.save(
          ChatParticipant.builder()
              .chatRoom(chatRoom)
              .member(member)
              .build()));

      isEnter = true;
    }

    //채팅방 참여하고 있는 인원 리스트
    List<ChatParticipantDto> chatParticipants = chatRoom.getChatParticipants()
        .stream()
        .map(ChatParticipantDto::fromEntity).toList();

    return ChatRoomEnterResponse.builder()
        .roomName(chatRoom.getName())
        .chatRoomId(chatRoomId)
        .managerId(chatRoom.getManagerId())
        .userCount(chatRoom.getUserCount())
        .chatParticipants(chatParticipants)
        .performanceName(PerformanceResponse.fromEntity(chatRoom.getPerformance()).getName())
        .enterStatus(isEnter ? NEW : ALREADY)
        .build();
  }

  @Override
  @Transactional
  public void exitChatRoom(CustomUserDetails customUserDetails, Long chatRoomId) {
    ChatParticipant participant = findChatParticipantOrThrow(chatRoomId, customUserDetails.getId());

    deleteChattingData(chatRoomId, participant);

    ChatRoomResponse response = ChatRoomResponse.fromEntity(participant.getChatRoom());

    //채팅방 인원이 전부 나간 경우 or 채팅방 방장이 방을 없앤 경우
    if (response.userCount() <= 0 || response.managerId().equals(customUserDetails.getId())) {
      chatRoomRepository.delete(participant.getChatRoom());
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Slice<ChatMessageDto> findAllMessageChatRoom(CustomUserDetails customUserDetails,
      ChatLastMessageRequest request, Long chatRoomId) {

    ChatParticipant chatParticipant = findChatParticipantOrThrow(chatRoomId, customUserDetails.getId());

    Slice<ChatMessage> message = chatMessageRepository.findChatRoomMessage(
        request.lastMsgId(), chatRoomId,
        Pageable.ofSize(request.limit() != 0 ? request.limit() : CHAT_MESSAGE_PAGE_SIZE));

    return message.map(m -> ChatMessageDto.fromEntity(m, chatParticipant));
  }

  @Override
  public Page<ChatRoomResponse> findAllTagNameChatRoom(String performanceId, String tagName,
      Pageable pageable) {
    return chatRoomRepository.findAllTagNameChatRoom(performanceId, tagName, pageable)
        .map(ChatRoomResponse::fromEntity);
  }

  @Override
  @Transactional
  public ChatRoomResponse kickChatRoom(CustomUserDetails customUserDetails, Long chatRoomId,
      Long memberId) {
    //현재 채팅방에서 강퇴할 인원 참여 여부 체크
    ChatParticipant chatParticipant = findChatParticipantOrThrow(chatRoomId, memberId);

    if (participantRepository.checkRoomManagerId(customUserDetails.getId())) {
      deleteChattingData(chatRoomId, chatParticipant);
    }

    return ChatRoomResponse.fromEntity(chatParticipant.getChatRoom());
  }

  private ChatParticipant findChatParticipantOrThrow(Long chatRoomId, Long memberId) {
    return participantRepository.findByMemberIdAndChatRoomId(memberId, chatRoomId)
        .orElseThrow(() -> new CustomException(PARTICIPANT_NOT_FOUND));
  }

  private void deleteChattingData(Long chatRoomId, ChatParticipant chatParticipant) {
    List<ChatMessage> messages = chatMessageRepository.findAllByChatRoomIdAndChatParticipantId(
        chatRoomId, chatParticipant.getId());

    if (!CollectionUtils.isEmpty(messages)) {
      chatMessageRepository.deleteAll(messages);
    }

    participantRepository.delete(chatParticipant);
    chatParticipant.getChatRoom().removeChatParticipant(chatParticipant);
  }

}
