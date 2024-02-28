package com.halfgallon.withcon.domain.chat.config.Handler;

import static com.halfgallon.withcon.domain.auth.constant.AuthConstant.ACCESS_TOKEN_PREFIX;
import static com.halfgallon.withcon.domain.chat.constant.ChattingConstant.CHATROOM_SESSION;
import static com.halfgallon.withcon.global.exception.ErrorCode.MEMBER_NOT_FOUND;

import com.halfgallon.withcon.domain.auth.entity.AccessToken;
import com.halfgallon.withcon.domain.auth.repository.AccessTokenRepository;
import com.halfgallon.withcon.domain.auth.security.service.CustomUserDetails;
import com.halfgallon.withcon.domain.chat.dto.ChatRoomSessionDto;
import com.halfgallon.withcon.domain.chat.entity.ChatRoom;
import com.halfgallon.withcon.domain.chat.repository.ChatMessageRepository;
import com.halfgallon.withcon.domain.chat.repository.ChatParticipantRepository;
import com.halfgallon.withcon.domain.chat.repository.ChatRoomRepository;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.domain.member.repository.MemberRepository;
import com.halfgallon.withcon.domain.notification.service.RedisService;
import com.halfgallon.withcon.global.exception.CustomException;
import com.halfgallon.withcon.global.exception.ErrorCode;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompPreHandler implements ChannelInterceptor {

  private final MemberRepository memberRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final AccessTokenRepository accessTokenRepository;
  private final ChatParticipantRepository participantRepository;
  private final ChatMessageRepository chatMessageRepository;

  private final RedisService redisService;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor =
        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    assert accessor != null;
    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      String token = resolveToken(accessor.getFirstNativeHeader("Authorization"));

      log.info("[preSend] stomp connection : {}", token);

      AccessToken findAccessToken = findAccessTokenOrThrow(token);

      Member member = findMemberOrThrow(findAccessToken.getMemberId());

      // WS Header save to userData
      accessor.setUser(authenticate(member));

      log.info("[preSend] stomp connection => user : {}, sessionId : {}",
          Objects.requireNonNull(accessor.getUser()).getName(),
          accessor.getSessionId());

    } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
      log.info("[preSend] stomp subscribe. destination: {}, user: {}, sessionId : {}",
          accessor.getDestination(),
          Objects.requireNonNull(accessor.getUser()).getName(),
          accessor.getSessionId());

      String sessionId = accessor.getSessionId();
      log.info("[preSend] stomp subscribe sessionId : {}", sessionId);

      Member member = memberRepository.findByUsername(accessor.getUser().getName())
          .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

      Long roomId = getRoomIdFromDestination(Objects.requireNonNull(accessor.getDestination()));
      log.info("[preSend] stomp subscribe roomId : {}", roomId);

      ChatRoom chatRoom = findChatRoomOrThrow(roomId);

      saveSession(chatRoom.getId(), member.getId(), sessionId);
      log.info("[preSend] stomp subscribe redis 구독 저장 완료");

    } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
      String sessionId = accessor.getSessionId();
      ChatRoomSessionDto sessionDto = redisService.getChatRoomHashKey(CHATROOM_SESSION, sessionId);

      try {
        assert sessionDto != null;
        ChatRoom chatRoom = findChatRoomOrThrow(sessionDto.chatRoomId());
        Member member = findMemberOrThrow(sessionDto.memberId());

        //채팅방 뒤로 가기 진행 시에 마지막 메시지 ID 업데이트 진행
        participantRepository.findByMemberIdAndChatRoomId(member.getId(), chatRoom.getId())
                .ifPresent(p ->
                    chatMessageRepository.findTopByChatRoomIdOrderBySendAtDesc(chatRoom.getId())
                    .ifPresent(chatMessage -> {
                      p.updateLastReadId(chatMessage.getId());
                      participantRepository.save(p);
                      log.info("[preSend] stomp disconnect 마지막 메시지 저장 완료");
                    })
                );

        redisService.deleteHashKey(CHATROOM_SESSION, sessionId);
        log.info("[preSend] stomp disconnect deleteHashKey 완료");

      } catch (Exception e) {
        log.error("[preSend] stomp disconnect is occurred : {}", e.getMessage());
      }

    }

    return message;
  }

  private String resolveToken(String token) {
    if (StringUtils.hasText(token) && token.startsWith(ACCESS_TOKEN_PREFIX)) {
      return token.substring(ACCESS_TOKEN_PREFIX.length());
    }
    throw new CustomException(ErrorCode.ACCESS_TOKEN_NOT_SUPPORTED);
  }

  private Member findMemberOrThrow(Long memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
  }

  private ChatRoom findChatRoomOrThrow(Long chatRoomId) {
    return chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
  }

  private AccessToken findAccessTokenOrThrow(String token) {
    return accessTokenRepository.findByAccessToken(token)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCESS_TOKEN_EXPIRED));
  }

  private Authentication authenticate(Member member) {
    CustomUserDetails principal = CustomUserDetails.fromEntity(member);
    Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "",
        principal.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);

    return authentication;
  }

  private Long getRoomIdFromDestination(String destination) {
    return Long.parseLong(destination.substring(destination.lastIndexOf(".") + 1));
  }

  private void saveSession(Long chatRoomId, Long memberId, String sessionId) {
    ChatRoomSessionDto sessionDto = ChatRoomSessionDto.builder()
        .chatRoomId(chatRoomId)
        .memberId(memberId)
        .build();

    redisService.updateToHash(CHATROOM_SESSION, sessionId, sessionDto);
  }

}
