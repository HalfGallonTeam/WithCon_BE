package com.halfgallon.withcon.domain.notification.service;

import com.halfgallon.withcon.domain.chat.dto.ChatRoomSessionDto;
import java.util.Map;

public interface RedisService {

  // hashKey로 조회
  Map<Object, Object> getHashByKey(String key);

  // HashKey - Map 구조 저장
  void saveToHash(String hashKey, Map<Object, Object> data, int hourTime);

  // HashKey - Map 값 변경
  void updateToHash(String hashKey, Object field, Object value);

  ChatRoomSessionDto getChatRoomHashKey(String key, String sessionId);

  void deleteHashKey(String hashKey, Object value);
}
