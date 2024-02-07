package com.halfgallon.withcon.domain.auth.client.kakao;

import static org.springframework.cloud.openfeign.encoding.HttpEncoding.CONTENT_TYPE;

import com.halfgallon.withcon.domain.auth.client.kakao.dto.request.KaKaoAccessTokenRequest;
import com.halfgallon.withcon.domain.auth.client.kakao.dto.response.KakaoAccessTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "KakaoLoginClient", url = "https://kauth.kakao.com/oauth")
public interface KakaoLoginClient {

  /**
   * 접근 토큰 발급 요청 메서드
   */
  @PostMapping("/token")
  KakaoAccessTokenResponse generateAccessToken(
      @RequestHeader(CONTENT_TYPE) String contentType,
      @SpringQueryMap KaKaoAccessTokenRequest kaKaoAccessTokenRequest);
}
