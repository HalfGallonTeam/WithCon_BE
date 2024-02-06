package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.member.dto.MemberDto;
import java.util.List;
import lombok.Builder;

@Builder
public record ChatRoomEnterResponse(
    Long chatRoomId,
    String roomName,
    Integer userCount,
    List<MemberDto> members
) {

}
