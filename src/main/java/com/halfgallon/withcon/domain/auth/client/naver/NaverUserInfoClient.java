package com.halfgallon.withcon.domain.auth.client.naver;

import static com.halfgallon.withcon.domain.auth.constant.AuthConstant.ACCESS_TOKEN_HEADER_NAME;

import com.halfgallon.withcon.domain.auth.client.naver.dto.response.NaverUserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "NaverUserInfoClient", url = "https://openapi.naver.com/v1/nid")
public interface NaverUserInfoClient {

  /**
   * 액세스 토큰을 이용하여 프로필 API 호출 메서드
   */
  @GetMapping("/me")
  NaverUserInfoResponse getUserInfo(@RequestHeader(ACCESS_TOKEN_HEADER_NAME) String accessToken);
}
