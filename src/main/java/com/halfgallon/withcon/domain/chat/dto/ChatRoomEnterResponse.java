package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.chat.constant.EnterStatus;
import java.util.List;
import lombok.Builder;

@Builder
public record ChatRoomEnterResponse(
    Long managerId,
    Long chatRoomId,
    String roomName,
    Integer userCount,
    EnterStatus enterStatus,
    String performanceName,
    List<ChatParticipantDto> chatParticipants
) {

}