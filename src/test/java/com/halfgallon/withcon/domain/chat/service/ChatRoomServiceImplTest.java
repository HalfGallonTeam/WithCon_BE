package com.halfgallon.withcon.domain.chat.service;

import static com.halfgallon.withcon.global.exception.ErrorCode.ALREADY_PARTICIPANT_CHATTING;
import static com.halfgallon.withcon.global.exception.ErrorCode.DUPLICATE_CHATROOM;
import static com.halfgallon.withcon.global.exception.ErrorCode.USER_JUST_ONE_CREATE_CHATROOM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.halfgallon.withcon.domain.chat.dto.ChatRoomEnterResponse;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomRequest;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomResponse;
import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import com.halfgallon.withcon.domain.chat.repository.ChatParticipantRepository;
import com.halfgallon.withcon.domain.chat.repository.ChatRoomRepository;
import com.halfgallon.withcon.domain.chat.service.impl.ChatRoomServiceImpl;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.global.exception.CustomException;
import com.halfgallon.withcon.global.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

  private Member member;
  @BeforeEach
  void setUp() {
    member = Member.builder()
        .id(1L)
        .username("test1234")
        .phoneNumber("010-1234-5678")
        .password("12345")
        .email("test@test.com")
        .build();
  }

  @Test
  @DisplayName("채팅방 생성 성공")
  void createChatRoom_Success() {
    //given
    ChatRoomRequest request = new ChatRoomRequest(1L, "1번채팅방");

    ChatRoom chatRoom = ChatRoom.builder()
        .id(1L)
        .name("1번 채팅방")
        .build();

    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));

    given(chatRoomRepository.existsByName(anyString()))
        .willReturn(false);

    given(chatParticipantRepository.existRoomLeader(anyLong()))
        .willReturn(false);

    given(chatRoomRepository.save(any()))
        .willReturn(ChatRoom.builder()
            .id(1L)
            .name("1번채팅방")
            .build());

    given(chatParticipantRepository.save(any()))
        .willReturn(ChatParticipant.builder()
            .chatRoom(chatRoom)
            .member(member)
            .isManager(true)
            .build());

    //when
    ChatRoomResponse response = chatRoomService.createChatRoom(request);

    //then
    assertThat(response.name()).isEqualTo(request.name());
  }

  @Test
  @DisplayName("채팅방 생성 실패 - 동일한 채팅방 이름 존재")
  void createChatRoom_FailByExistName() {
    //given
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));

    given(chatRoomRepository.existsByName(anyString()))
        .willReturn(true);

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> chatRoomService.createChatRoom(new ChatRoomRequest(1L, "1번채팅방")));

    //then
    assertThat(DUPLICATE_CHATROOM.getStatus()).isEqualTo(customException.getStatusCode());
  }

  @Test
  @DisplayName("채팅방 생성 실패 - 1인당 1개만 생성 가능")
  void createChatRoom_FailByJustOne() {
    //given
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));

    given(chatParticipantRepository.existRoomLeader(anyLong()))
        .willReturn(true);

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> chatRoomService.createChatRoom(new ChatRoomRequest(1L, "1번채팅방")));

    //then
    assertThat(USER_JUST_ONE_CREATE_CHATROOM.getStatus()).isEqualTo(customException.getStatusCode());
  }


  @Test
  @DisplayName("채팅방 조회 성공")
  void findChatRoom() {
    //given
    ChatRoom chatRoom = ChatRoom.builder()
        .id(1L)
        .name("1번 채팅방")
        .build();

    given(chatRoomRepository.findAll())
        .willReturn(List.of(chatRoom));

    //when
    List<ChatRoomResponse> responses = chatRoomService.findChatRoom();

    //then
    assertThat(responses.size()).isOne();
  }


  @Test
  @DisplayName("채팅방 입장 실패 - 이미 참여하고 있는 채팅")
  void enterChatRoom_failByAlreadyChat() {
    //given
    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));

    given(chatRoomRepository.findById(anyLong()))
        .willReturn(Optional.of(ChatRoom.builder()
                .id(1L)
                .name("1번채팅방")
                .build()));

    given(chatParticipantRepository.existsByMemberIdAndChatRoomId(anyLong(), anyLong()))
        .willReturn(true);

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> chatRoomService.enterChatRoom(1L, 1L));

    //then
    assertThat(ALREADY_PARTICIPANT_CHATTING.getDescription()).isEqualTo(customException.getMessage());
  }

  @Test
  @DisplayName("채팅방 입장 성공")
  void enterChatRoom_Success() {
    //given
    ChatRoom chatRoom = ChatRoom.builder()
        .id(1L)
        .name("1번채팅방")
        .build();

    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));
    given(chatRoomRepository.findById(anyLong()))
        .willReturn(Optional.of(chatRoom));

    given(chatParticipantRepository.existsByMemberIdAndChatRoomId(anyLong(), anyLong()))
        .willReturn(false);

    given(chatParticipantRepository.save(any()))
        .willReturn(ChatParticipant.builder()
            .chatRoom(chatRoom)
            .member(member)
            .build());

    //when
    ChatRoomEnterResponse response = chatRoomService.enterChatRoom(1L, 1L);

    //then
    assertThat(chatRoom.getId()).isEqualTo(response.chatRoomId());
    assertThat(response.userCount()).isOne();   //채팅방 유저 1명
  }


  @Test
  @DisplayName("채팅방 퇴장 실패 - 해당 채팅방 참여자가 아닙니다.")
  void exitChatRoom_FailByParticipantNotFound() {
    //given
    ChatRoom chatRoom = ChatRoom.builder()
        .id(1L)
        .name("1번채팅방")
        .build();

    given(memberRepository.findById(anyLong()))
        .willReturn(Optional.of(member));
    given(chatRoomRepository.findById(anyLong()))
        .willReturn(Optional.of(chatRoom));

    given(chatParticipantRepository.findByMemberAndChatRoom(member, chatRoom))
        .willReturn(Optional.empty());

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> chatRoomService.exitChatRoom(1L, 1L));

    //then
    assertThat(ErrorCode.PARTICIPANT_NOT_FOUND.getDescription()).isEqualTo(customException.getMessage());
  }

}