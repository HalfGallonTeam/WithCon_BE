package com.halfgallon.withcon.domain.chat.dto;

import com.halfgallon.withcon.domain.member.entity.Member;
import java.util.List;
import lombok.Builder;

@Builder
public record ChatRoomEnterResponse(
    Long chatRoomId,
    String chatRoomName,
    Integer userCount,
    List<Member> members  //추후에 entity -> dto 변경 예정
) {

}
