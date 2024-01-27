package com.halfgallon.withcon.domain.chat.repository;

public interface CustomChatParticipantRepository {

  boolean checkRoomManager(Long memberId);

}
