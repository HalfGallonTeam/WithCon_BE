package com.halfgallon.withcon.domain.member.repository;

import com.halfgallon.withcon.domain.member.constant.LoginType;
import com.halfgallon.withcon.domain.member.entity.Member;
import com.halfgallon.withcon.global.config.JpaAuditingConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaAuditingConfig.class)
class MemberRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;

  @Test
  @DisplayName("존재하는 유저네임이면 true 를 리턴한다.")
  void existsByUsername_success() {
    // given
    String username = "username";

    Member member = createDefaultMember();

    memberRepository.save(member);

    // when
    boolean exist = memberRepository.existsByUsername(username);

    // then
    Assertions.assertThat(exist).isTrue();
  }

  @Test
  @DisplayName("존재하는 이메일이면 true 를 리턴한다.")
  void existsByEmail_success() {
    // given
    String email = "test@example.com";

    Member member = createDefaultMember();

    memberRepository.save(member);

    // when
    boolean exist = memberRepository.existsByEmail(email);

    // then
    Assertions.assertThat(exist).isTrue();
  }

  @Test
  @DisplayName("존재하는 닉네임이면 true 를 리턴한다.")
  void existsByNickname_success() {
    // given
    String nickname = "nickname";

    Member member = createDefaultMember();

    memberRepository.save(member);

    // when
    boolean exist = memberRepository.existsByNickname(nickname);

    // then
    Assertions.assertThat(exist).isTrue();
  }

  @Test
  @DisplayName("존재하는 이메일이면 true 를 리턴한다.")
  void existsByPhoneNumber_success() {
    // given
    String PhoneNumber = "01012345678";

    Member member = createDefaultMember();

    memberRepository.save(member);

    // when
    boolean exist = memberRepository.existsByPhoneNumber(PhoneNumber);

    // then
    Assertions.assertThat(exist).isTrue();
  }

  private Member createDefaultMember() {
    return Member.builder()
        .id(1L)
        .email("test@example.com")
        .username("username")
        .password("1q2w3e4r!")
        .loginType(LoginType.HOME)
        .nickname("위드콘")
        .phoneNumber("01012345678")
        .build();
  }
}