package com.halfgallon.withcon.domain.notification.service;

import java.util.Map;

public interface RedisCacheService {

  // hashKey로 조회
  Map<Object, Object> getHashByKey(String key);

  // HashKey - Map 구조 저장
  void saveToHash(String hashKey, Map<Object, Object> data, int hourTime);

  // HashKey - Map 값 변경
  void updateToHash(String hashKey, Object field, Object value);

  void deleteToHash(String hashKey);

}
