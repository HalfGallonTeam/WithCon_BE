package com.halfgallon.withcon.domain.chat.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record ChatRoomEnterResponse(
    Long chatRoomId,
    Long performanceId,
    String roomName,
    String managerName,
    Integer userCount,
    List<ChatParticipantDto> chatParticipants
) {

}
