package com.halfgallon.withcon.domain.chat.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record ChatRoomEnterResponse(
    Long chatRoomId,
    String roomName,
    Integer userCount,
    List<ChatParticipantDto> chatParticipants
) {

}
