package com.halfgallon.withcon.domain.member.dto.request;

public record UpdateMemberRequest(
    String nickname,
    String phoneNumber,
    String newPassword
) {

}
