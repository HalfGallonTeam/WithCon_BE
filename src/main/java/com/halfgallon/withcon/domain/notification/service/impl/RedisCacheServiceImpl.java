package com.halfgallon.withcon.domain.notification.service.impl;

import com.halfgallon.withcon.domain.notification.service.RedisCacheService;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCacheServiceImpl implements RedisCacheService {

  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public Map<Object, Object> getHashByKey(String key) {
    HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
    return hashOps.entries(key);
  }

  @Override
  public void saveToHash(String hashKey, Map<Object, Object> data, int hourTime) {
    redisTemplate.opsForHash().putAll(hashKey, data);

    redisTemplate.expire(hashKey, Duration.ofHours(hourTime));
  }

  @Override
  public void updateToHash(String hashKey, Object field, Object value) {
    redisTemplate.opsForHash().put(hashKey, field, value);
  }

  @Override
  public void deleteToHash(String hashKey) {
    redisTemplate.delete(hashKey);
  }
}
