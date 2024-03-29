package com.halfgallon.withcon.domain.chat.service.impl;

import static com.halfgallon.withcon.global.exception.ErrorCode.DUPLICATE_CHATROOM_NAME;
import static com.halfgallon.withcon.global.exception.ErrorCode.USER_JUST_ONE_CREATE_CHATROOM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.chat.constant.MessageType;
import com.halfgallon.withcon.domain.chat.dto.ChatMessageDto;
import com.halfgallon.withcon.domain.chat.dto.ChatLastMessageRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomEnterResponse;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomResponse;
import com.halfgallon.withcon.domain.chat.entity.ChatMessage;
import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import com.halfgallon.withcon.domain.chat.repository.ChatMessageRepository;
import com.halfgallon.withcon.domain.chat.repository.ChatParticipantRepository;
import com.halfgallon.withcon.domain.chat.repository.ChatRoomRepository;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.domain.performance.entitiy.Performance;
import com.halfgallon.withcon.domain.performance.repository.PerformanceRepository;
import com.halfgallon.withcon.domain.tag.entity.Tag;
import com.halfgallon.withcon.domain.tag.repository.TagRepository;
import com.halfgallon.withcon.global.exception.CustomException;
import com.halfgallon.withcon.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
class ChatRoomServiceImplTest {

  @InjectMocks
  ChatRoomServiceImpl chatRoomService;
  @Mock
  ChatRoomRepository chatRoomRepository;
  @Mock
  MemberRepository memberRepository;
  @Mock
  ChatParticipantRepository chatParticipantRepository;
  @Mock
  TagRepository tagRepository;
  @Mock
  ChatMessageRepository chatMessageRepository;
  @Mock
  PerformanceRepository performanceRepository;

  private Member member;
  private CustomUserDetails customUserDetails;

  @BeforeEach
  void setUp() {
    member = Member.builder()
        .id(1L)
        .username("test1234")
        .phoneNumber("010-1234-5678")
        .password("12345")
        .build();

    customUserDetails = CustomUserDetails.fromEntity(member);
  }

  @Test
  @Disabled
  @DisplayName("채팅방 생성 성공")
  void createChatRoom_Success() {
    //given
    ChatRoomRequest request = new ChatRoomRequest("1번채팅방", "1",
        List.of("#1번방", "#2번방"));

    ChatRoom chatRoom = ChatRoom.builder()
        .id(1L)
        .name("1번채팅방")
        .build();

    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));

    given(chatRoomRepository.existsByNameAndPerformance_Id(anyString(), anyString()))
        .willReturn(false);

    given(chatParticipantRepository.checkRoomManagerId(anyLong()))
        .willReturn(false);

    Performance performance = Performance.builder()
        .id("123456")
        .name("1번 공연")
        .build();

    given(performanceRepository.findById(any()))
        .willReturn(Optional.of(performance));

    given(chatRoomRepository.save(any()))
        .willReturn(ChatRoom.builder()
            .id(1L)
            .name("1번채팅방")
            .build());

    given(chatParticipantRepository.save(any()))
        .willReturn(ChatParticipant.builder()
            .chatRoom(chatRoom)
            .member(member)
            .build());

    given(tagRepository.saveAll(any()))
        .willReturn(List.of(Tag.builder()
            .name("#1번방")
            .build()));

    //when
    ChatRoomResponse response = chatRoomService.createChatRoom(customUserDetails, request);

    //then
    assertThat(response.tags().size()).isNotZero();
    assertThat(response.roomName()).isEqualTo(request.roomName());
  }

  @Test
  @DisplayName("채팅방 생성 실패 - 동일한 채팅방 이름 존재합니다.")
  void createChatRoom_FailByExistName() {
    //given
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));

    given(performanceRepository.findById(anyString()))
        .willReturn(Optional.of(Performance.builder()
            .id("123456")
            .name("1번 공연")
            .build()));

    given(chatRoomRepository.existsByNameAndPerformance_Id(anyString(), anyString()))
        .willReturn(true);

    ChatRoomRequest request = new ChatRoomRequest("1번 채팅방", "1",
        List.of("#1번방", "#2번방"));

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> chatRoomService.createChatRoom(customUserDetails, request));

    //then
    assertThat(DUPLICATE_CHATROOM_NAME.getStatus()).isEqualTo(customException.getErrorCode().getStatus());
  }

  @Test
  @Disabled
  @DisplayName("채팅방 생성 실패 - 1인당 1개만 생성 가능합니다.(해당 조건 미사용)")
  void createChatRoom_FailByJustOne() {
    //given
    ChatRoomRequest request = new ChatRoomRequest("1번 채팅방", "1",
        List.of("#1번방", "#2번방"));

    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));

    given(chatRoomRepository.existsByNameAndPerformance_Id(anyString(),anyString()))
        .willReturn(true);

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> chatRoomService.createChatRoom(customUserDetails, request));

    //then
    assertThat(USER_JUST_ONE_CREATE_CHATROOM.getStatus()).isEqualTo(customException.getErrorCode().getStatus());
  }


  @Test
  @DisplayName("채팅방 조회 성공 - 페이징 처리")
  void findChatRoom() {
    //given
    Performance performance = Performance.builder()
        .id("123456")
        .name("1번 공연")
        .build();

    ChatRoom chatRoom = ChatRoom.builder()
        .id(1L)
        .name("1번 채팅방")
        .performance(performance)
        .build();

    Pageable pageable = PageRequest.of(0, 10, Sort.by(DESC, "createdAt"));

    given(chatRoomRepository.findAllByPerformance_Id(performance.getId(), pageable))
        .willReturn(new PageImpl<>(List.of(chatRoom)));

    //when
    Page<ChatRoomResponse> responses = chatRoomService.findChatRoom("123456", pageable);

    //then
    assertTrue(responses.hasContent());
  }

  @Test
  @DisplayName("채팅방 입장 성공")
  void enterChatRoom_Success() {
    //given
    Performance performance = Performance.builder()
        .id("123456")
        .name("1번 공연")
        .build();

    ChatRoom chatRoom = ChatRoom.builder()
        .id(1L)
        .name("1번채팅방")
        .performance(performance)
        .build();

    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));
    given(chatRoomRepository.findById(anyLong()))
        .willReturn(Optional.of(chatRoom));
    given(chatParticipantRepository.save(any()))
        .willReturn(ChatParticipant.builder()
            .chatRoom(chatRoom)
            .member(member)
            .build());

    //when
    ChatRoomEnterResponse response = chatRoomService.enterChatRoom(customUserDetails, 1L
    );

    //then
    assertThat(chatRoom.getId()).isEqualTo(response.chatRoomId());
    assertThat(response.userCount()).isNotNull();
  }


  @Test
  @DisplayName("채팅방 퇴장 실패 - 해당 채팅방 참여자가 아닙니다.")
  void exitChatRoom_FailByParticipantNotFound() {
    //given
    given(chatParticipantRepository.findByMemberIdAndChatRoomId(1L, 1L))
        .willReturn(Optional.empty());

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> chatRoomService.exitChatRoom(customUserDetails, 1L));

    //then
    assertThat(ErrorCode.PARTICIPANT_NOT_FOUND.getDescription()).isEqualTo(customException.getMessage());
  }

  @Test
  @DisplayName("채팅방 퇴장 성공")
  void exitChatRoom_Success() {
    //given
    Performance performance = Performance.builder()
        .id("123456")
        .name("1번 공연")
        .build();

    ChatRoom chatRoom = ChatRoom.builder()
        .id(1L)
        .name("1번채팅방")
        .managerId(member.getId())
        .performance(performance)
        .build();

    ChatParticipant participant = ChatParticipant.builder()
        .id(1L)
        .member(member)
        .chatRoom(chatRoom)
        .build();

    given(chatParticipantRepository.findByMemberIdAndChatRoomId(1L, 1L))
        .willReturn(Optional.of(participant));

    //when
    chatRoomService.exitChatRoom(customUserDetails, 1L);

    //then
    verify(chatParticipantRepository, times(1)).delete(participant);
  }


  @Test
  @DisplayName("채팅 메시지 조회")
  void findAllMessageChatRoom_firstPage() {
    //given
    ChatLastMessageRequest request = new ChatLastMessageRequest(null, null);

    ChatRoom chatRoom = ChatRoom.builder()
        .id(1L)
        .name("1번채팅방")
        .build();

    ChatParticipant participant = ChatParticipant.builder()
        .chatRoom(chatRoom)
        .member(member)
        .build();

    given(chatParticipantRepository.findByMemberIdAndChatRoomId(member.getId(), chatRoom.getId()))
        .willReturn(Optional.of(participant));

    given(chatMessageRepository.findChatRoomMessage(request.lastMsgId(), 1L, Pageable.ofSize(10)))
        .willReturn(new SliceImpl<>(List.of(ChatMessage.builder()
            .room(chatRoom)
            .chatParticipant(participant)
            .message("test")
            .messageType(MessageType.CHAT)
            .build())));

    //when
    Slice<ChatMessageDto> messages = chatRoomService.findAllMessageChatRoom(
        customUserDetails, request, 1L);

    //then
    assertThat(messages.hasContent()).isTrue();
  }


  @Test
  @Disabled
  @DisplayName("채팅방 강퇴 성공 - 방장인 경우")
  void kickChatRoom_Success() {
    //given
    List<ChatParticipant> participants = new ArrayList<>();
    for (int i = 0; i < 9; i++) {
      participants.add(ChatParticipant.builder()
          .build());
    }

    Member user = Member.builder()
        .id(2L)
        .username("test1234")
        .phoneNumber("010-1234-5678")
        .password("12345")
        .build();

    ChatRoom chatRoom = ChatRoom.builder()
        .id(1L)
        .name("1번채팅방")
        .userCount(10)
        .performance(Performance.builder()
            .id("123456")
            .name("1번 공연")
            .build())
        .chatParticipants(participants)
        .build();


    given(chatParticipantRepository.findByMemberIdAndChatRoomId(anyLong(), anyLong()))
        .willReturn(Optional.of(ChatParticipant.builder()
            .chatRoom(chatRoom)
            .member(user)
            .build()));

    given(chatParticipantRepository.checkRoomManagerId(anyLong()))
        .willReturn(true);

    //when
    ChatRoomResponse response = chatRoomService.kickChatRoom(customUserDetails, chatRoom.getId(), user.getId());

    //then
    assertThat(response.userCount()).isEqualTo(9);
  }
}