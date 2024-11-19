package com.halfgallon.withcon.domain.auth.client.naver;

import com.halfgallon.withcon.domain.auth.client.naver.dto.request.NaverAccessTokenRequest;
import com.halfgallon.withcon.domain.auth.client.naver.dto.response.NaverAccessTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "NaverLoginClient", url = "https://nid.naver.com/oauth2.0")
public interface NaverLoginClient {

  /**
   * 접근 토큰 발급 요청 메서드
   */
  @PostMapping("/token")
  NaverAccessTokenResponse generateAccessToken(
      @SpringQueryMap NaverAccessTokenRequest naverAccessTokenRequest);

}
