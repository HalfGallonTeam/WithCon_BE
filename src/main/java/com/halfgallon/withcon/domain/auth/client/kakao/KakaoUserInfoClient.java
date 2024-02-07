package com.halfgallon.withcon.domain.auth.client.kakao;

import static com.halfgallon.withcon.domain.auth.constant.AuthConstant.ACCESS_TOKEN_HEADER_NAME;
import static org.springframework.cloud.openfeign.encoding.HttpEncoding.CONTENT_TYPE;

import com.halfgallon.withcon.domain.auth.client.kakao.dto.response.KakaoUserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "KakaoUserInfoClient", url = "https://kapi.kakao.com/v2/user")
public interface KakaoUserInfoClient {

  /**
   * 액세스 토큰을 이용하여 프로필 API 호출 메서드
   */
  @GetMapping("/me")
  KakaoUserInfoResponse getUserInfo(
      @RequestHeader(CONTENT_TYPE) String contentType,
      @RequestHeader(ACCESS_TOKEN_HEADER_NAME) String accessToken);
}
