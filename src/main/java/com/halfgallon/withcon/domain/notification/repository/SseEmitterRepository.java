package com.halfgallon.withcon.domain.notification.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class SseEmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    // SseEmitter 객체 저장
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
      emitters.put(emitterId, sseEmitter);
      return sseEmitter;
    }

    // 특정 회원의 모든 이벤트 조회
    public Map<String, SseEmitter> findAllByMemberIdStartsWith(String memberId) {
      return emitters.entrySet().stream()
          .filter(entry -> entry.getKey().startsWith(memberId))
          .collect(Collectors.toMap(Map.Entry :: getKey, Map.Entry::getValue));
    }

    // 객체 삭제
    public void deleteById(String emitterId) {
      emitters.remove(emitterId);
    }

    // 특정 회원의 모든 이벤트 삭제
    public void deleteAllByMemberId(String memberId) {
      emitters.entrySet()
          .removeIf(entry -> entry.getKey().startsWith(memberId));
    }



}
