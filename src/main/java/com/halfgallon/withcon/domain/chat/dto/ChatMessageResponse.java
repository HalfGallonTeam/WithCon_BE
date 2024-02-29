package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.constant.MessageType;
import com.halfgallon.withcon.domain.chat.entity.ChatMessage;
import com.halfgallon.withcon.domain.chat.entity.ChatParticipant;
import lombok.Builder;

@Builder
public record ChatMessageResponse (
    Long memberId,
    Long roomId,
    Long messageId,
    String message,
    MessageType messageType,
    String nickName,
    String userProfile,
    Long sendAt
) {
  public static ChatMessageResponse fromEntity(ChatMessage chatMessage, ChatParticipant chatParticipant) {
    return ChatMessageResponse.builder()
        .roomId(chatMessage.getChatRoom().getId())
        .messageId(chatMessage.getId())
        .message(chatMessage.getMessage())
        .messageType(chatMessage.getMessageType())
        .sendAt(chatMessage.getSendAt())
        .memberId(chatParticipant.getMember().getId())
        .nickName(chatParticipant.getMember().getNickname())
        .userProfile(chatParticipant.getMember().getProfileImage())
        .build();
  }
}
