package com.halfgallon.withcon.domain.chat.dto;

import lombok.Builder;

@Builder
public record ChatRoomSessionDto(
    Long memberId,
    Long chatRoomId
) {

}
